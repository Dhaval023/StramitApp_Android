package com.example.stramitapp.ui.load_shipment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoadShipmentViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is load shipment Fragment"
    }
    val text: LiveData<String> = _text
}