package com.example.stramitapp.ui.floorsweep

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stramitapp.model.FloorSweepModel
import com.example.stramitapp.model.FloorSweepResultListModel
import com.example.stramitapp.restclient.SyncClientService
import com.example.stramitapp.services.API.request.FloorSweepRequest
import com.example.stramitapp.utilities.AppSettings
import com.example.stramitapp.zebraconnection.RFIDHandler
import com.zebra.rfid.api3.TagData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FloorSweepViewModel : ViewModel() {

    private val _clientService = SyncClientService()

    private val _floorSweepDateEntry = MutableLiveData(Calendar.getInstance().time)
    val floorSweepDateEntry: LiveData<Date> = _floorSweepDateEntry

    private val _scannedItemsList = MutableLiveData<MutableList<FloorSweepModel>>(mutableListOf())
    val scannedItemsList: LiveData<MutableList<FloorSweepModel>> = _scannedItemsList

    private val _scannedItemsResultList = MutableLiveData<List<FloorSweepResultListModel>>()
    val scannedItemsResultList: LiveData<List<FloorSweepResultListModel>> = _scannedItemsResultList

    private val _isDatePickerEnable = MutableLiveData(true)
    val isDatePickerEnable: LiveData<Boolean> = _isDatePickerEnable

    private val _isBusy = MutableLiveData(false)
    val isBusy: LiveData<Boolean> = _isBusy

    private val _isPageEnabled = MutableLiveData(true)
    val isPageEnabled: LiveData<Boolean> = _isPageEnabled

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _navigationToResult = MutableLiveData<List<FloorSweepResultListModel>?>()
    val navigationToResult: LiveData<List<FloorSweepResultListModel>?> = _navigationToResult

    private val tagListDict = mutableMapOf<String, Int>()

    var companyId: Int = 0

    init {
        val selectedSystem = AppSettings.tempSelectedSystem ?: AppSettings.selectedSystem
        companyId = selectedSystem?.companyId ?: 0
    }

    fun setFloorSweepDate(date: Date) {
        _floorSweepDateEntry.value = date
    }

    fun saveDate() {
        _isDatePickerEnable.value = false
    }

    fun enableDatePicker() {
        _isDatePickerEnable.value = true
    }

    fun clearAll() {
        _scannedItemsList.value?.clear()
        _scannedItemsList.postValue(_scannedItemsList.value)
        tagListDict.clear()
    }

    fun submitEvent() {
        if (_isDatePickerEnable.value == true) {
            _errorMessage.value = "ALERT: Please Save the Date."
            return
        }

        val items = _scannedItemsList.value ?: emptyList<FloorSweepModel>()
        if (items.isEmpty()) {
            _errorMessage.value = "ALERT: No scanned items. Please scan and try again."
            return
        }

        viewModelScope.launch {
            try {
                _isBusy.value = true
                _isPageEnabled.value = false
                
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dateString = sdf.format(_floorSweepDateEntry.value ?: Date())

                val request = FloorSweepRequest().apply {
                    deliveryDate = dateString
                    assetTag = items.map { it.rfid }
                }

                delay(500) // Match C# await Task.Delay(500)

                val result = _clientService.floorSweepDataTransfer(request)
                if (result.list == null) {
                    _errorMessage.value = "ALERT: No items found!"
                } else if (result.statusCode == 1) {
                    val resultList = result.list?.map { item ->
                        FloorSweepResultListModel(
                            id = item.id ?: "",
                            rfid = item.rfid ?: "",
                            co = item.co ?: "",
                            deliveryDate = item.deliveryDate ?: "",
                            productSKU = item.productSKU ?: ""
                        )
                    } ?: emptyList()

                    _scannedItemsResultList.value = resultList
                    _navigationToResult.value = resultList
                } else {
                    _errorMessage.value = "ALERT: No items found!"
                }
            } catch (ex: Exception) {
                Log.e("FloorSweepViewModel", "Submit Error", ex)
                _errorMessage.value = "ERROR: Failed to Submit: ${ex.message}"
            } finally {
                _isBusy.value = false
                _isPageEnabled.value = true
            }
        }
    }

    fun onBarcodeScanned(barcode: String) {
        if (barcode.isBlank()) return

        val currentList = _scannedItemsList.value ?: mutableListOf()
        if (currentList.none { it.rfid.equals(barcode, ignoreCase = true) }) {
            currentList.add(FloorSweepModel(rfid = barcode))
            _scannedItemsList.value = currentList
        }
    }

    fun onTagRead(tags: Array<TagData>) {
        val currentList = _scannedItemsList.value ?: mutableListOf()
        var updated = false

        for (tag in tags) {
            val tagId = tag.tagID ?: continue
            if (tagListDict.containsKey(tagId)) {
                tagListDict[tagId] = (tagListDict[tagId] ?: 0) + tag.tagSeenCount
            } else {
                tagListDict[tagId] = tag.tagSeenCount
                if (currentList.none { it.rfid.equals(tagId, ignoreCase = true) }) {
                    currentList.add(FloorSweepModel(rfid = tagId))
                    updated = true
                }
            }
        }

        if (updated) {
            _scannedItemsList.postValue(currentList)
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun onNavigationDone() {
        _navigationToResult.value = null
    }
}
