package com.example.stramitapp.ui.load_shipment

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.stramitapp.model.Asset
import com.example.stramitapp.model.AssetMovementInfo
import com.example.stramitapp.model.CompanyLocation
import com.example.stramitapp.utilities.AppSettings
import com.example.stramitapp.Repositories.AssetDataStore
import com.example.stramitapp.Repositories.AssetMovementInfoDataStore
import com.example.stramitapp.services.APIHelper
import com.example.stramitapp.services.SyncService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ShipmentAddTruckViewModel(application: Application) : AndroidViewModel(application) {

    private val assetDataStore = AssetDataStore()
    private val movementInfoDataStore = AssetMovementInfoDataStore()

    var scannedItems: List<Asset> = emptyList()
    var shipmentNumber: String = ""

    private val _truckNumberText = MutableLiveData<String>()
    val truckNumberText: LiveData<String> = _truckNumberText
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    private val _uiEvent = MutableLiveData<String?>()
    val uiEvent: LiveData<String?> = _uiEvent
    private val _locationList = MutableLiveData<List<CompanyLocation>>()
    val locationList: LiveData<List<CompanyLocation>> = _locationList

    fun onTruckNumberTapped() {
        viewModelScope.launch {
            try {
                val companyId = AppSettings.tempSelectedSystem?.companyId
                    ?: AppSettings.selectedSystem?.companyId

                if (companyId == null) {
                    _uiEvent.value = "ERROR:No company selected. Please select a company first."
                    return@launch
                }

                val locations = AppSettings.database.companyLocationDao()
                    .getItemByCompany(companyId)

                _locationList.value = locations
                _uiEvent.value = "SHOW_DROPDOWN"
            } catch (e: Exception) {
                _uiEvent.value = "ERROR:Failed to load locations: ${e.message}"
            }
        }
    }
    fun onLocationSelected(location: CompanyLocation) {
        AppSettings.tempSelectedLocation = location
        _truckNumberText.value = location.locationName.orEmpty()
    }

    fun submitShipmentTruckEvent() {
        viewModelScope.launch {
            try {
                val truckText = _truckNumberText.value.orEmpty()
                if (truckText.isBlank()) {
                    _uiEvent.value = "ERROR:Please fill Truck Number before proceeding."
                    return@launch
                }
                val selectedLocation = AppSettings.tempSelectedLocation
                if (selectedLocation?.locationId == null) {
                    _uiEvent.value = "ERROR:Truck Number is not Valid."
                    return@launch
                }
                if (scannedItems.isEmpty()) {
                    _uiEvent.value = "ERROR:No scanned items. Please scan and try again."
                    return@launch
                }
                _truckNumberText.value = selectedLocation.locationName!!
                val destinationLocationId = selectedLocation.locationId!!

                var isSuccess = false
                val movementInfoList = mutableListOf<AssetMovementInfo>()
                val updatedAssets = mutableListOf<Asset>()

                try {
                    for (item in scannedItems) {
                        if (item == null) continue

                        val originalLocationId      = item.locationId
                        val originalUpdateFlag      = item.updateFlag
                        val originalLastUpdatedBy   = item.lastUpdatedBy
                        val originalLastUpdateDeviceId = item.lastUpdateDeviceId
                        val originalLastUpdateDate  = item.lastUpdateDate
                        val originalCustom18        = item.custom18

                        val sourceLocationId        = item.locationId ?: 0
                        item.locationId             = destinationLocationId
                        item.lastUpdatedBy          = AppSettings.authenticatedUser?.userId
                        item.lastUpdateDeviceId     = AppSettings.deviceId
                        item.lastUpdateDate         = APIHelper.nowDateTimeSQLite()
                        item.updateFlag             = "S" // "I"
                        item.custom18               = shipmentNumber

                        val itemUpdateSuccess = assetDataStore.updateItemAsync(item)
                        if (!itemUpdateSuccess) {
                            item.locationId             = originalLocationId
                            item.updateFlag             = originalUpdateFlag
                            item.lastUpdatedBy          = originalLastUpdatedBy
                            item.lastUpdateDeviceId     = originalLastUpdateDeviceId
                            item.lastUpdateDate         = originalLastUpdateDate
                            item.custom18               = originalCustom18

                            throw Exception("Failed to update asset ${item.assetId} in local database")
                        }

                        updatedAssets.add(item)

                        if (sourceLocationId != destinationLocationId) {
                            val movementInfo = AssetMovementInfo().apply {
                                assetId              = item.assetId
                                this.sourceLocationId      = sourceLocationId
                                this.destinationLocationId = destinationLocationId
                                this.deviceId        = item.deviceId  // Using item's device ID as per comment
                                movedBy              = AppSettings.authenticatedUser?.userId
                                movementRecordedBy   = AppSettings.authenticatedUser?.userId
                                movementDate         = APIHelper.nowDateTimeSQLite()
                                updatedBy            = AppSettings.authenticatedUser?.userId ?: 0
                                lastUpdateDate       = APIHelper.nowDateTimeSQLite()
                                updateFlag           = "I"
                                flagSync             = item.flagSync ?: 0
                                attributeDeviceId    = item.deviceId
                                movementType         = "in"
                            }

                            val isItemAdded = movementInfoDataStore.addItemAsync(movementInfo)
                            if (isItemAdded) {
                                movementInfoList.add(movementInfo)
                            } else {
                                throw Exception("Failed to add movement info for asset ${item.assetId}")
                            }
                        }
                    }

                    isSuccess = true

                } catch (ex: Exception) {
                    isSuccess = false

                    for (movementInfo in movementInfoList) {
                        try {
                            movementInfoDataStore.deleteItemAsync(movementInfo)
                        } catch (deleteEx: Exception) {
                            // Log deletion error but continue rollback
                            Log.d("ShipmentTruckVM", "Error during rollback - deleting movement info: ${deleteEx.message}")
                        }
                    }
                    throw Exception("Error processing shipment items: ${ex.message}", ex)
                }

                if (isSuccess) {
                    try {
                        _isLoading.value = true
                        val service = SyncService()
                        service.forceSync()
                        _uiEvent.value = "SUCCESS:Shipment moved successfully."

                    } catch (syncEx: Exception) {
                        Log.d("ShipmentTruckVM", "Sync failed but local operations completed: ${syncEx.message}")

                    } finally {
                        _isLoading.value = false
                    }
                } else {
                    _uiEvent.value = "FAILED:An error occurred while moving assets. Changes have been rolled back."
                }

            } catch (ex: Exception) {
                // Silently catch outer exception — matches C# behavior: catch (Exception ex) { }
            }
        }
    }
    fun onUiEventConsumed() {
        _uiEvent.value = null
    }

    private fun nowDateTimeSQLite(): String =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

    private data class AssetOriginalValues(
        val asset:              Asset,
        val locationId:         Int?,
        val updateFlag:         String?,
        val lastUpdatedBy:      Int?,
        val lastUpdateDeviceId: Int,
        val lastUpdateDate:     String?,
        val custom18:           String?
    )
}