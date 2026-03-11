package com.example.stramitapp.ui.addlocationmovement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stramitapp.model.Company
import com.example.stramitapp.model.CompanyLocation
import com.example.stramitapp.models.Database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddLocationMovementViewModel : ViewModel() {

    private val _companies = MutableStateFlow<List<Company>>(emptyList())
    val companies: StateFlow<List<Company>> = _companies

    private val _locations = MutableStateFlow<List<CompanyLocation>>(emptyList())
    val locations: StateFlow<List<CompanyLocation>> = _locations

    fun loadCompanies() {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    AppDatabase.getInstance().companyDao().getAll()
                }
                android.util.Log.d("DB_DEBUG", "Companies loaded: ${result.size}")
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
                android.util.Log.d("DB_DEBUG", "Locations loaded: ${result.size}")
                _locations.value = result
            } catch (e: Exception) {
                android.util.Log.e("DB_DEBUG", "loadLocations error: ${e.message}")
            }
        }
    }
}