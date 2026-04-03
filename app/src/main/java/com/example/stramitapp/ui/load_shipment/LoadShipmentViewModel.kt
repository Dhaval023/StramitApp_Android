package com.example.stramitapp.ui.load_shipment

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.stramitapp.repositories.AssetDataStore
import kotlinx.coroutines.launch

class LoadShipmentViewModel(application: Application) : AndroidViewModel(application) {

    private val assetDataStore = AssetDataStore()
    private val _scannedItems = MutableLiveData<MutableList<String>>(mutableListOf())
    val scannedItems: LiveData<MutableList<String>> = _scannedItems

    private val _shipmentNumber = MutableLiveData("")
    val shipmentNumber: LiveData<String> = _shipmentNumber

    sealed class UiEvent {
        data class NavigateToList(val shipmentNumber: String) : UiEvent()
        data class ShowError(val message: String) : UiEvent()
    }

    private val _uiEvent = MutableLiveData<UiEvent?>()
    val uiEvent: LiveData<UiEvent?> = _uiEvent

    fun onEventConsumed() {
        _uiEvent.value = null
    }

    fun onShipmentNumberChanged(value: String) {
        _shipmentNumber.value = value
    }

    fun onNextClicked() {
        val number = _shipmentNumber.value?.trim().orEmpty()

        if (number.isBlank()) {
            _uiEvent.value =
                UiEvent.ShowError("Please fill Shipment Number field before proceeding.")
            return
        }

        viewModelScope.launch {
            val shipment = assetDataStore.checkIfShipmentNumberExist(number)

            if (shipment == null) {
                _uiEvent.value =
                    UiEvent.ShowError("Shipment Number does not exist. Please try again.")
            } else {
                Log.d("SHIPMENT_DEBUG", "FOUND: ${shipment.shipmentNumber}")
                _uiEvent.value = UiEvent.NavigateToList(number)
            }
        }
    }
    fun onItemBarcodeScanned(barcode: String) {
        if (barcode.isBlank()) return

        val current = _scannedItems.value ?: mutableListOf()

        if (current.contains(barcode)) {
            return
        }

        current.add(barcode)
        _scannedItems.postValue(current)
    }

    fun clearAll() {
        _scannedItems.value = mutableListOf()
    }

    fun deleteItemAt(index: Int) {
        val current = _scannedItems.value ?: return
        if (index < 0 || index >= current.size) return
        current.removeAt(index)
        _scannedItems.postValue(current)
    }
    fun onBarcodeScanned(barcode: String) {
        if (barcode.isBlank()) return

        viewModelScope.launch {
            val shipment = assetDataStore.checkIfShipmentNumberExist(barcode)

            if (shipment != null) {
                _shipmentNumber.postValue(barcode)
            } else {
                Log.d("SHIPMENT_DEBUG", "SCANNED BUT NOT FOUND")
                _uiEvent.postValue(
                    UiEvent.ShowError("Scanned Shipment does not exist.")
                )
            }
        }
    }
}