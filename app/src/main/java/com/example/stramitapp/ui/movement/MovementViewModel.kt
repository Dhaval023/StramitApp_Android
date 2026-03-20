package com.example.stramitapp.ui.movement

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stramitapp.model.Asset
import com.example.stramitapp.Repositories.AssetDataStore
import com.example.stramitapp.Repositories.AssetMovementInfoDataStore
import com.example.stramitapp.model.AssetMovementInfo
import com.example.stramitapp.models.Database.AppDatabase
import com.example.stramitapp.services.App
import com.example.stramitapp.services.SyncService
import com.example.stramitapp.utilities.AppSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovementViewModel : ViewModel() {

    private val assetDataStore = AssetDataStore()
    private val assetMovementInfoDataStore = AssetMovementInfoDataStore()
    private val _scannedAssets = MutableLiveData<List<Asset>>(emptyList())
    val scannedAssets: LiveData<List<Asset>> = _scannedAssets

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _isBusy = MutableLiveData(false)
    val isBusy: LiveData<Boolean> = _isBusy
    private val internalList = mutableListOf<Asset>()

    fun onBarcodeScanned(barcode: String) {
        if (barcode.isBlank()) return

        viewModelScope.launch {
            try {
                val companyId = AppSettings.tempSelectedSystem?.companyId ?: run {
                    _toastMessage.value = "No company selected. Please select a company first."
                    return@launch
                }

                val alreadyInList = internalList.any { it.barcode == barcode }
                if (alreadyInList) {
                    _toastMessage.value = "Barcode already scanned"
                    return@launch
                }

                val asset = withContext(Dispatchers.IO) {
                    assetDataStore.getItemByBarcodeAsync(companyId, barcode)
                }

                if (asset == null) {
                    _toastMessage.value = "Item not found. Please try again."
                    Log.d("MovementVM", "Asset not found for barcode: $barcode")
                    return@launch
                }

                val destinationLocationId = AppSettings.tempSelectedLocation?.locationId
                if (asset.locationId == destinationLocationId) {
                    _toastMessage.value = "Asset was not moved. Please change its destination location to proceed."
                    Log.d("MovementVM", "Asset location unchanged, skipping: $barcode")
                    return@launch
                }

                internalList.add(asset)
                _scannedAssets.value = internalList.toList()
                _toastMessage.value = "Asset added: ${asset.title ?: barcode}"
                Log.d("MovementVM", "Asset added: ${asset.title}, total: ${internalList.size}")

            } catch (e: Exception) {
                Log.e("MovementVM", "Error processing barcode: $barcode", e)
                _toastMessage.value = "Error processing barcode. Please try again."
            }
        }
    }

    fun onTagScanned(tagId: String) {
        if (tagId.isBlank()) return

        viewModelScope.launch {
            try {
                val companyId = AppSettings.tempSelectedSystem?.companyId ?: run {
                    _toastMessage.value = "No company selected. Please select a company first."
                    return@launch
                }

                val alreadyInList = internalList.any {
                    it.tag?.uppercase() == tagId.uppercase()
                }
                if (alreadyInList) {
                    return@launch
                }

                val asset = withContext(Dispatchers.IO) {
                    assetDataStore.getItemByTagAsync(companyId, tagId)
                }

                if (asset == null) {
                    Log.d("MovementVM", "Asset not found for tag: $tagId")
                    return@launch
                }

                val destinationLocationId = AppSettings.tempSelectedLocation?.locationId
                if (asset.locationId == destinationLocationId) {
                    Log.d("MovementVM", "Asset location unchanged, skipping tag: $tagId")
                    return@launch
                }

                internalList.add(asset)
                _scannedAssets.value = internalList.toList()
                Log.d("MovementVM", "Tag asset added: ${asset.title}, total: ${internalList.size}")

            } catch (e: Exception) {
                Log.e("MovementVM", "Error processing tag: $tagId", e)
            }
        }
    }

    fun submitMovement() {
        val assets = internalList.toList()

        if (assets.isEmpty()) {
            _toastMessage.value = "No scanned items. Please scan and try again."
            return
        }

        viewModelScope.launch {
            try {
                _isBusy.value = true

                val destinationLocationId = AppSettings.tempSelectedLocation?.locationId ?: run {
                    _toastMessage.value = "No destination location selected."
                    _isBusy.value = false
                    return@launch
                }

                val authenticatedUser = AppSettings.authenticatedUser ?: run {
                    _toastMessage.value = "User not authenticated. Please login again."
                    _isBusy.value = false
                    return@launch
                }

                var isSuccess = false
                val addedMovementInfoList = mutableListOf<AssetMovementInfo>()
                val nowDateStr = getCurrentUtcDateString()

                for (item in assets) {
                    val sourceLocationId = item.locationId ?: 0

                    item.locationId = destinationLocationId
                    item.lastUpdatedBy = authenticatedUser.userId
                    item.lastUpdateDeviceId = AppSettings.deviceId
                    item.lastUpdateDate = nowDateStr

                    assetDataStore.updateItemAsync(item)

                    val movementInfo = AssetMovementInfo().apply {
                        assetId = item.assetId
                        this.sourceLocationId = sourceLocationId
                        this.destinationLocationId = destinationLocationId
                        deviceId = item.deviceId
                        movedBy = authenticatedUser.userId
                        movementRecordedBy = authenticatedUser.userId
                        movementDate = nowDateStr
                        updatedBy = authenticatedUser.userId
                        lastUpdateDate = nowDateStr
                        updateFlag = "I"
                        flagSync = item.flagSync ?: 0
                        attributeDeviceId = item.deviceId
                        movementType = "in"
                        workOrderNumber = ""
                    }

                    val isItemAdded = withContext(Dispatchers.IO) {
                        assetMovementInfoDataStore.addItemAsync(movementInfo)
                    }

                    Log.d("MovementVM", "addItemAsync result: $isItemAdded for assetId: ${item.assetId}")

                    if (isItemAdded) {
                        addedMovementInfoList.add(movementInfo)
                        isSuccess = true
                    } else {
                        isSuccess = false
                        break
                    }
                }

                if (isSuccess) {
                    clearAll()
                    _toastMessage.value = "Asset moved successfully."

                    withContext(Dispatchers.IO) {
                        try {
                            val syncService = SyncService()
                            syncService.forceSync()
                        } catch (ex: Exception) {
                            Log.e("MovementVM", "Post-submit sync error: ${ex.message}", ex)
                        } finally {
                            try {
                                val app = AppSettings.appContext
                                if (app is com.example.stramitapp.App) {
                                    app.reinitializeRepository()
                                }
                            } catch (ex: Exception) {
                                Log.e("MovementVM", "DB reinit failed: ${ex.message}", ex)
                            }
                        }
                    }

                } else {
                    withContext(Dispatchers.IO) {
                        for (movementInfo in addedMovementInfoList) {
                            assetMovementInfoDataStore.deleteItemAsync(movementInfo)
                        }
                    }
                    _toastMessage.value = "An error occurred while moving asset."
                }

            } catch (e: Exception) {
                Log.e("MovementVM", "submitMovement EXCEPTION: ${e.javaClass.simpleName} — ${e.message}", e)
                _toastMessage.value = "Error: ${e.message}"
            } finally {
                _isBusy.value = false
            }
        }
    }

    private fun getCurrentUtcDateString(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.US)
        sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
        return sdf.format(java.util.Date())
    }

    fun deleteItemAt(position: Int) {
        if (position in internalList.indices) {
            internalList.removeAt(position)
            _scannedAssets.value = internalList.toList()
            _toastMessage.value = "Item deleted"
        }
    }

    fun clearAll() {
        internalList.clear()
        _scannedAssets.value = emptyList()
        _toastMessage.value = "All items cleared"
    }

    fun getCount(): Int = internalList.size
}