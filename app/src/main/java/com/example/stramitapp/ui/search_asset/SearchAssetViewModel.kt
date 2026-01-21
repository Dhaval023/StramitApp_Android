package com.example.stramitapp.ui.search_asset

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchAssetViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is search asset Fragment"
    }
    val text: LiveData<String> = _text
}