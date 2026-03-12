package com.example.stramitapp.ui.search_asset

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
                android.util.Log.e("DB_DEBUG", "loadCompanies error: ${e.message}")
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
                android.util.Log.e("DB_DEBUG", "loadLocations error: ${e.message}")
            }
        }
    }

    fun search(companyId: Int, locationId: Int, barcode: String) {
        viewModelScope.launch {
            _isLoading.value = true
//            _errorMessage.value = null
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
                            locationName = locName,
                            companyAssetId = asset.companyAssetId ?: "",
                            custom13 = asset.custom13 ?: "",
                            barcode = asset.barcode ?: "",
                            custom18 = asset.custom18 ?: "",
                            assetId = asset.assetId
                        )
                    }
                }
                if (results.isEmpty()) {
                    _errorMessage.emit("No results found.")
                } else {
                    _searchResults.emit(results)
                }
            } catch (e: Exception) {
                android.util.Log.e("DB_DEBUG", "search error: ${e.message}")
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