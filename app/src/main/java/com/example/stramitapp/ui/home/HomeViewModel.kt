package com.example.stramitapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stramitapp.model.Company
import com.example.stramitapp.models.Database.AppDatabase
import com.example.stramitapp.ui.addlocationmovement.AddLocationMovementViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private val _texts = MutableLiveData<List<String>>().apply {
        value = (1..16).mapIndexed { _, i ->
            "This is item # $i"
        }
    }

    val texts: LiveData<List<String>> = _texts

    private val _companies = MutableStateFlow<List<Company>>(emptyList())
    val companies: StateFlow<List<Company>> = _companies

    fun loadCompanies() {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    AppDatabase.getInstance().companyDao().getAll()
                }
                _companies.value = result
            } catch (e: Exception) {
                Log.e("DB_DEBUG", "Home loadCompanies error: ${e.message}")
            }
        }
    }
}