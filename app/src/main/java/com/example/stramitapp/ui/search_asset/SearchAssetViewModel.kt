package com.example.stramitapp.ui.search_asset

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stramitapp.model.Company
import com.example.stramitapp.model.CompanyLocation
import com.example.stramitapp.models.Database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchAssetViewModel : ViewModel() {

    private val _companies = MutableStateFlow<List<Company>>(emptyList())
    val companies: StateFlow<List<Company>> = _companies
    private val _locations = MutableStateFlow<List<CompanyLocation>>(emptyList())
    val locations: StateFlow<List<CompanyLocation>> = _locations
    private val _searchResults = MutableSharedFlow<List<SearchResultItem>>(replay = 0)
    val searchResults: SharedFlow<List<SearchResultItem>> = _searchResults
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _errorMessage = MutableSharedFlow<String>(replay = 0)
    val errorMessage: SharedFlow<String> = _errorMessage
    fun loadCompanies() {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    AppDatabase.getInstance().companyDao().getAll()
                }
                _companies.value = result
            } catch (e: Exception) {
                Log.e("DB_DEBUG", "loadCompanies error: ${e.message}")
            }
        }
    }

    fun loadLocationsByCompany(companyId: Int) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    AppDatabase.getInstance().companyLocationDao().getItemByCompany(companyId)
                }
                _locations.value = result
            } catch (e: Exception) {
                Log.e("DB_DEBUG", "loadLocations error: ${e.message}")
            }
        }
    }

    fun search(companyId: Int, locationId: Int, barcode: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val results = withContext(Dispatchers.IO) {
                    val assets = AppDatabase.getInstance().assetDao()
                        .searchAssets(companyId, locationId, barcode)

                    assets.map { asset ->
                        val locName = if (asset.locationId != null && asset.locationId != 0) {
                            AppDatabase.getInstance().companyLocationDao()
                                .getById(asset.locationId!!)?.locationName ?: ""
                        } else ""

                        SearchResultItem(
                            assetId = asset.assetId,
                            locationName = locName,
                            barcode = asset.barcode        ?: "",
                            companyAssetId = asset.companyAssetId ?: "",
                            title = asset.title          ?: "",  // Product Description
                            tag = asset.tag            ?: "",
                            serialNumber = asset.serialNumber   ?: "",
                            assetValue = asset.assetValue?.toString() ?: "",
                            weight = asset.weight?.toString()     ?: "",
                            weightUom = asset.weightUom?.toString()  ?: "",
                            longDesc = asset.longDesc       ?: "",
                            purchaseDate = asset.purchaseDate   ?: "",  // Departure Date
                            createDate = asset.createDate     ?: "",  // Received Date
                            custom1 = asset.custom1        ?: "",  // Length
                            custom2 = asset.custom2        ?: "",  // Width
                            custom3 = asset.custom3        ?: "",  // Girth
                            custom4 = asset.custom4        ?: "",  // Colour
                            custom5 = asset.custom5        ?: "",  // Number of Bends
                            custom6 = asset.custom6        ?: "",  // Pack Number
                            custom7 = asset.custom7        ?: "",  // Total Pack Number
                            custom8 = asset.custom8        ?: "",  // Split Number
                            custom9 = asset.custom9        ?: "",  // Supplier Reference
                            custom10 = asset.custom10       ?: "",  // Supplier Number
                            custom11 = asset.custom11       ?: "",  // Docket Number
                            custom12 = asset.custom12       ?: "",  // PO Number
                            custom13 = asset.custom13       ?: "",  // M3 CO
                            custom14 = asset.custom14       ?: "",  // Customer Name
                            custom15 = asset.custom15       ?: "",  // Customer Reference
                            custom16 = asset.custom16       ?: "",  // Delivery Number
                            custom17 = asset.custom17       ?: "",  // Drop Number
                            custom18 = asset.custom18       ?: "",  // Shipment Number
                            custom19 = asset.custom19       ?: "",  // Route
                            custom20 = asset.custom20       ?: "",  // Delivery Instruction
                            custom21 = asset.custom21       ?: "",  // Address
                            custom22 = asset.custom22       ?: "",  // M3 DO
                            custom23 = asset.custom23       ?: "",  // Quantity alt
                            custom24 = asset.custom24       ?: "",  // QuantityUOM
                            custom25 = asset.custom25       ?: "",  // LengthUOM
                            custom26 = asset.custom26       ?: "",  // HeightUOM
                            custom27 = asset.custom27       ?: "",  // WidthUOM
                            custom28 = asset.custom28       ?: "",  // WeightUOM
                            custom29 = asset.custom29       ?: "",  // GirthUOM
                            custom30 = asset.custom30       ?: "",  // ColourUOM
                            custom31 = asset.custom31       ?: "",  // Package Structure
                            custom32 = asset.custom32       ?: "",  // Manufacturing Instruction
                            custom33 = asset.custom33       ?: "",  // Schedule Number
                            custom34 = asset.custom34       ?: "",  // Mark Number
                            custom35 = asset.custom35       ?: "",  // Pack Description
                            custom36 = asset.custom36       ?: ""   // Package Number
                        )
                    }
                }
                if (results.isEmpty()) {
                    _errorMessage.emit("No results found.")
                } else {
                    _searchResults.emit(results)
                }
            } catch (e: Exception) {
                Log.e("DB_DEBUG", "search error: ${e.message}")
                _errorMessage.emit( "Search failed: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun reset() {
        viewModelScope.launch {
            _searchResults.emit(emptyList())
        }
    }
}