package com.example.stramitapp.ui.search_shipment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchShipmentViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is search shipment Fragment"
    }
    val text: LiveData<String> = _text
}