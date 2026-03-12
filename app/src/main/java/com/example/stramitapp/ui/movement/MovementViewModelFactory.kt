package com.example.stramitapp.ui.movement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MovementViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovementViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovementViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}