package com.example.stramitapp.ui.load_shipment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stramitapp.model.Asset
import com.example.stramitapp.models.Database.AppDatabase
import com.example.stramitapp.ui.search_shipment.ShipmentSearchResultItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShipmentItemResultViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<ShipmentSearchResultItem>>(emptyList())
    val items: StateFlow<List<ShipmentSearchResultItem>> = _items

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isEmpty = MutableStateFlow(false)
    val isEmpty: StateFlow<Boolean> = _isEmpty

    fun loadResults(shipmentNumber: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val results = withContext(Dispatchers.IO) {
                    val assets: List<Asset> = AppDatabase.getInstance()
                        .assetDao()
                        .getShipmentResultAssets(shipmentNumber = shipmentNumber)

                    assets.map { asset ->
                        val locName = if (asset.locationId != null && asset.locationId != 0) {
                            AppDatabase.getInstance()
                                .companyLocationDao()
                                .getById(asset.locationId!!)?.locationName ?: ""
                        } else ""

                        ShipmentSearchResultItem(
                            assetId        = asset.assetId,
                            locationName   = locName,
                            custom13       = asset.custom13 ?: "",
                            custom22       = asset.custom22 ?: "",
                            companyAssetId = asset.companyAssetId ?: "",
                            custom17       = asset.custom17 ?: "",
                            barcode        = asset.barcode  ?: "",
                            title          = asset.title    ?: "",
                            custom1        = asset.custom1  ?: "",
                            custom2        = asset.custom2  ?: "",
                            custom3        = asset.custom3  ?: "",
                            custom4        = asset.custom4  ?: "",
                            custom5        = asset.custom5  ?: "",
                            custom6        = asset.custom6  ?: "",
                            custom7        = asset.custom7  ?: "",
                            custom8        = asset.custom8  ?: "",
                            custom9        = asset.custom9  ?: "",
                            custom10       = asset.custom10 ?: "",
                            custom11       = asset.custom11 ?: "",
                            custom12       = asset.custom12 ?: "",
                            custom14       = asset.custom14 ?: "",
                            custom15       = asset.custom15 ?: "",
                            custom16       = asset.custom16 ?: "",
                            custom18       = asset.custom18 ?: "",
                            custom19       = asset.custom19 ?: "",
                            custom20       = asset.custom20 ?: "",
                            custom21       = asset.custom21 ?: "",
                            custom23       = asset.custom23 ?: "",
                            custom24       = asset.custom24 ?: "",
                            custom25       = asset.custom25 ?: "",
                            custom26       = asset.custom26 ?: "",
                            custom27       = asset.custom27 ?: "",
                            custom28       = asset.custom28 ?: "",
                            custom29       = asset.custom29 ?: "",
                            custom30       = asset.custom30 ?: "",
                            custom31       = asset.custom31 ?: "",
                            custom32       = asset.custom32 ?: "",
                            custom33       = asset.custom33 ?: "",
                            custom34       = asset.custom34 ?: "",
                            custom35       = asset.custom35 ?: "",
                            custom36       = asset.custom36 ?: "",
                            weight         = asset.weight?.toString()     ?: "",
                            weightUom      = asset.weightUom?.toString()  ?: "",
                            assetValue     = asset.assetValue?.toString() ?: "",
                            purchaseDate   = asset.purchaseDate ?: "",
                            createDate     = asset.createDate   ?: ""
                        )
                    }
                }

                _items.value   = results
                _isEmpty.value = results.isEmpty()

            } catch (e: Exception) {
                android.util.Log.e("ShipmentItemResultVM", "loadResults error: ${e.message}", e)
                _items.value   = emptyList()
                _isEmpty.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }
}