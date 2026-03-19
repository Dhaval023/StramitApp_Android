//package com.example.stramitapp.ui.floorsweep
//
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.stramitapp.model.FloorSweepModel
//import com.example.stramitapp.model.FloorSweepResultListModel
//import com.example.stramitapp.restclient.SyncClientService
//import com.example.stramitapp.services.API.request.FloorSweepRequest
//import com.example.stramitapp.utilities.AppSettings
//import com.zebra.rfid.api3.TagData
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import java.text.SimpleDateFormat
//import java.util.*
//
//class FloorSweepViewModel : ViewModel() {
//
//    private val _clientService = SyncClientService()
//
//    private val _floorSweepDateEntry = MutableLiveData(Calendar.getInstance().time)
//    val floorSweepDateEntry: LiveData<Date> = _floorSweepDateEntry
//
//    // Immutable List — every postValue() is a new reference so observer always fires
//    private val _scannedItemsList = MutableLiveData<List<FloorSweepModel>>(emptyList())
//    val scannedItemsList: LiveData<List<FloorSweepModel>> = _scannedItemsList
//
//    private val _scannedItemsResultList = MutableLiveData<List<FloorSweepResultListModel>>()
//    val scannedItemsResultList: LiveData<List<FloorSweepResultListModel>> = _scannedItemsResultList
//
//    private val _isDatePickerEnable = MutableLiveData(true)
//    val isDatePickerEnable: LiveData<Boolean> = _isDatePickerEnable
//
//    private val _isBusy = MutableLiveData(false)
//    val isBusy: LiveData<Boolean> = _isBusy
//
//    private val _isPageEnabled = MutableLiveData(true)
//    val isPageEnabled: LiveData<Boolean> = _isPageEnabled
//
//    private val _errorMessage = MutableLiveData<String?>()
//    val errorMessage: LiveData<String?> = _errorMessage
//
//    private val _navigationToResult = MutableLiveData<List<FloorSweepResultListModel>?>()
//    val navigationToResult: LiveData<List<FloorSweepResultListModel>?> = _navigationToResult
//
//    // Private mutable working copy — never posted directly
//    private val internalList = mutableListOf<FloorSweepModel>()
//    private val tagListDict = mutableMapOf<String, Int>()
//
//    var companyId: Int = 0
//
//    init {
//        val selectedSystem = AppSettings.tempSelectedSystem ?: AppSettings.selectedSystem
//        companyId = selectedSystem?.companyId ?: 0
//    }
//
//    /** Always posts a brand-new List reference so LiveData observer fires every time */
//    private fun publishList() {
//        _scannedItemsList.postValue(internalList.toList())
//    }
//
//    fun setFloorSweepDate(date: Date) {
//        _floorSweepDateEntry.value = date
//    }
//
//    fun saveDate() {
//        _isDatePickerEnable.value = false
//    }
//
//    fun enableDatePicker() {
//        _isDatePickerEnable.value = true
//    }
//
//    /**
//     * Full reset — clears items, tag dictionary, and re-enables the date picker.
//     * Call this from onResume so returning to the fragment always starts clean.
//     */
//    fun resetAll() {
//        internalList.clear()
//        tagListDict.clear()
//        _isDatePickerEnable.value = true
//        publishList()
//    }
//
//    /** Called by the "delete all" button — clears list but keeps the saved date */
//    fun clearAll() {
//        internalList.clear()
//        tagListDict.clear()
//        publishList()
//    }
//
//    fun removeItem(item: FloorSweepModel) {
//        val removed = internalList.removeAll { it.rfid.equals(item.rfid, ignoreCase = true) }
//        if (removed) {
//            tagListDict.remove(item.rfid)
//            publishList()
//        }
//    }
//
//    fun submitEvent() {
//        if (_isDatePickerEnable.value == true) {
//            _errorMessage.value = "ALERT: Please Save the Date."
//            return
//        }
//        if (internalList.isEmpty()) {
//            _errorMessage.value = "ALERT: No scanned items. Please scan and try again."
//            return
//        }
//
//        viewModelScope.launch {
//            try {
//                _isBusy.value = true
//                _isPageEnabled.value = false
//
//                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//                val dateString = sdf.format(_floorSweepDateEntry.value ?: Date())
//
//                val request = FloorSweepRequest().apply {
//                    deliveryDate = dateString
//                    assetTag = internalList.map { it.rfid }
//                }
//
//                delay(500)
//
//                val result = _clientService.floorSweepDataTransfer(request)
//                if (result.list == null) {
//                    _errorMessage.value = "ALERT: No items found!"
//                } else if (result.statusCode == 1) {
//                    val resultList = result.list?.map { item ->
//                        FloorSweepResultListModel(
//                            id = item.id ?: "",
//                            rfid = item.rfid ?: "",
//                            co = item.co ?: "",
//                            deliveryDate = item.deliveryDate ?: "",
//                            productSKU = item.productSKU ?: ""
//                        )
//                    } ?: emptyList()
//
//                    _scannedItemsResultList.value = resultList
//                    _navigationToResult.value = resultList
//                } else {
//                    _errorMessage.value = "ALERT: No items found!"
//                }
//            } catch (ex: Exception) {
//                Log.e("FloorSweepViewModel", "Submit Error", ex)
//                _errorMessage.value = "ERROR: Failed to Submit: ${ex.message}"
//            } finally {
//                _isBusy.value = false
//                _isPageEnabled.value = true
//            }
//        }
//    }
//
//    fun onBarcodeScanned(barcode: String) {
//        if (barcode.isBlank()) return
//        if (internalList.none { it.rfid.equals(barcode, ignoreCase = true) }) {
//            internalList.add(FloorSweepModel(rfid = barcode))
//            publishList()
//        }
//    }
//
//    fun onTagRead(tags: Array<TagData>) {
//        var updated = false
//        for (tag in tags) {
//            val tagId = tag.tagID ?: continue
//            if (tagListDict.containsKey(tagId)) {
//                tagListDict[tagId] = (tagListDict[tagId] ?: 0) + tag.tagSeenCount
//            } else {
//                tagListDict[tagId] = tag.tagSeenCount
//                if (internalList.none { it.rfid.equals(tagId, ignoreCase = true) }) {
//                    internalList.add(FloorSweepModel(rfid = tagId))
//                    updated = true
//                }
//            }
//        }
//        if (updated) publishList()
//    }
//
//    fun clearErrorMessage() {
//        _errorMessage.value = null
//    }
//
//    fun onNavigationDone() {
//        _navigationToResult.value = null
//    }
//}
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
import com.zebra.rfid.api3.TagData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class FloorSweepViewModel : ViewModel() {

    private val _clientService = SyncClientService()

    private val _floorSweepDateEntry = MutableLiveData(Calendar.getInstance().time)
    val floorSweepDateEntry: LiveData<Date> = _floorSweepDateEntry

    private val _scannedItemsList = MutableLiveData<List<FloorSweepModel>>(emptyList())
    val scannedItemsList: LiveData<List<FloorSweepModel>> = _scannedItemsList

    private val _scannedItemsResultList = MutableLiveData<List<FloorSweepResultListModel>>()
    val scannedItemsResultList: LiveData<List<FloorSweepResultListModel>> = _scannedItemsResultList

    private val _isDatePickerEnable = MutableLiveData(true)
    val isDatePickerEnable: LiveData<Boolean> = _isDatePickerEnable

    // ✅ FIX: Use separate loading + error LiveData so UI can react independently
    private val _isBusy = MutableLiveData(false)
    val isBusy: LiveData<Boolean> = _isBusy

    private val _isPageEnabled = MutableLiveData(true)
    val isPageEnabled: LiveData<Boolean> = _isPageEnabled

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // ✅ FIX: navigationToResult must only be set on Main thread — use postValue safely
    private val _navigationToResult = MutableLiveData<List<FloorSweepResultListModel>?>()
    val navigationToResult: LiveData<List<FloorSweepResultListModel>?> = _navigationToResult

    private val internalList = mutableListOf<FloorSweepModel>()
    private val tagListDict = mutableMapOf<String, Int>()

    var companyId: Int = 0

    init {
        val selectedSystem = AppSettings.tempSelectedSystem ?: AppSettings.selectedSystem
        companyId = selectedSystem?.companyId ?: 0
    }

    private fun publishList() {
        _scannedItemsList.postValue(internalList.toList())
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

    fun resetAll() {
        internalList.clear()
        tagListDict.clear()
        _isDatePickerEnable.value = true
        publishList()
    }

    fun clearAll() {
        internalList.clear()
        tagListDict.clear()
        publishList()
    }

    fun removeItem(item: FloorSweepModel) {
        val removed = internalList.removeAll { it.rfid.equals(item.rfid, ignoreCase = true) }
        if (removed) {
            tagListDict.remove(item.rfid)
            publishList()
        }
    }

    fun submitEvent() {
        // ── Validations (run on Main thread, .value is safe here) ──────────────
        if (_isDatePickerEnable.value == true) {
            _errorMessage.value = "ALERT: Please Save the Date."
            return
        }
        if (internalList.isEmpty()) {
            _errorMessage.value = "ALERT: No scanned items. Please scan and try again."
            return
        }

        viewModelScope.launch {
            try {
                // ✅ Switch to Main for LiveData .value writes
                withContext(Dispatchers.Main) {
                    _isBusy.value = true
                    _isPageEnabled.value = false
                }

                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dateString = sdf.format(_floorSweepDateEntry.value ?: Date())

                val request = FloorSweepRequest().apply {
                    deliveryDate = dateString
                    assetTag = internalList.map { it.rfid }
                }

                delay(500)

                val result = withContext(Dispatchers.IO) {
                    _clientService.floorSweepDataTransfer(request)
                }

                withContext(Dispatchers.Main) {
                    if (result.list == null) {
                        _errorMessage.value = "ALERT: No items found!"
                    } else if (result.statusCode == 1) {
                        val resultList = result.list!!.map { item ->
                            FloorSweepResultListModel(
                                id = item.id ?: "",
                                rfid = item.rfid ?: "",
                                co = item.co ?: "",
                                deliveryDate = item.deliveryDate ?: "",
                                productSKU = item.productSKU ?: ""
                            )
                        }
                        _scannedItemsResultList.value = resultList

                        _navigationToResult.value = resultList
                    } else {
                        _errorMessage.value = "ALERT: No items found!"
                    }
                }

            } catch (ex: Exception) {
                Log.e("FloorSweepViewModel", "Submit Error", ex)
                withContext(Dispatchers.Main) {
                    _errorMessage.value = "ERROR: Failed to Submit: ${ex.message}"
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _isBusy.value = false
                    _isPageEnabled.value = true
                }
            }
        }
    }

    fun onBarcodeScanned(barcode: String) {
        if (barcode.isBlank()) return
        if (internalList.none { it.rfid.equals(barcode, ignoreCase = true) }) {
            internalList.add(FloorSweepModel(rfid = barcode))
            publishList()
        }
    }

    fun onTagRead(tags: Array<TagData>) {
        var updated = false
        for (tag in tags) {
            val tagId = tag.tagID ?: continue
            if (tagListDict.containsKey(tagId)) {
                tagListDict[tagId] = (tagListDict[tagId] ?: 0) + tag.tagSeenCount
            } else {
                tagListDict[tagId] = tag.tagSeenCount
                if (internalList.none { it.rfid.equals(tagId, ignoreCase = true) }) {
                    internalList.add(FloorSweepModel(rfid = tagId))
                    updated = true
                }
            }
        }
        if (updated) publishList()
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun onNavigationDone() {
        _navigationToResult.value = null
    }
}