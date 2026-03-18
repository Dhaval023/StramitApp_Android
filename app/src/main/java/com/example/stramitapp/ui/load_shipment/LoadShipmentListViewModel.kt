package com.example.stramitapp.ui.load_shipment

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.stramitapp.Repositories.AssetDataStore
import com.example.stramitapp.Repositories.AssetResult
import com.example.stramitapp.model.Asset
import com.example.stramitapp.utilities.AppSettings
import kotlinx.coroutines.launch

class ShipmentListViewModel(application: Application) : AndroidViewModel(application) {

    private val assetDataStore = AssetDataStore()

    private val _items = MutableLiveData<MutableList<Asset>>(mutableListOf())
    val items: LiveData<MutableList<Asset>> = _items

    private val _uiEvent = MutableLiveData<String?>()
    val uiEvent: LiveData<String?> = _uiEvent

    fun onEventConsumed() {
        _uiEvent.value = null
    }

    fun onItemScanned(barcode: String, shipmentNumber: String) {
        if (barcode.isBlank()) return

        val companyId = AppSettings.tempSelectedSystem?.companyId ?: run {
            _uiEvent.postValue("Company ID not found. Please login again.")
            return
        }

        viewModelScope.launch {
            val list = _items.value ?: mutableListOf()

            if (list.any { it.tag == barcode || it.barcode == barcode }) {
                Log.d("ShipmentListVM", "Duplicate skipped: $barcode")
                _uiEvent.postValue("Item already scanned.")
                return@launch
            }

            val asset: Asset? = when (
                val barcodeResult = assetDataStore.getShipmentItemByBarcodeOnlyAsync(
                    barcode, shipmentNumber, companyId
                )
            ) {
                is AssetResult.Success -> barcodeResult.asset
                    ?: when (
                        val tagResult = assetDataStore.getShipmentItemByTagOnlyAsync(
                            barcode, shipmentNumber, companyId
                        )
                    ) {
                        is AssetResult.Success -> tagResult.asset
                        is AssetResult.Error -> {
                            Log.e("ShipmentListVM", "Tag lookup failed", tagResult.exception)
                            null
                        }
                    }
                is AssetResult.Error -> {
                    Log.e("ShipmentListVM", "Barcode lookup failed", barcodeResult.exception)
                    null
                }
            }

            if (asset == null) {
                Log.d("ShipmentListVM", "Wrong item or not in shipment: $barcode")
                _uiEvent.postValue("Wrong Item scanned. Please scan the correct item.")
                return@launch
            }
            Log.d("ShipmentListVM", "Found: ${asset.tag} — ${asset.assetId}")
            list.add(asset)
            _items.postValue(list)
        }
    }

    fun deleteItem(asset: Asset) {
        val list = _items.value ?: return
        list.remove(asset)
        _items.value = list
    }

    fun clearAll() {
        _items.value = mutableListOf()
    }

    fun getCount(): Int = _items.value?.size ?: 0
}