package com.example.stramitapp.ui.movement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stramitapp.zebraconnection.Inventory.InventoryViewModel

class MovementViewModelFactory(private val inventoryViewModel: InventoryViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovementViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovementViewModel(inventoryViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}