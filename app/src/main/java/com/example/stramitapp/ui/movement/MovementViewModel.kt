package com.example.stramitapp.ui.movement

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.stramitapp.zebraconnection.Inventory.InventoryItem
import com.example.stramitapp.zebraconnection.Inventory.InventoryViewModel

class MovementViewModel(private val inventoryViewModel: InventoryViewModel) : ViewModel() {

    val inventoryItems: LiveData<ArrayList<InventoryItem>> = inventoryViewModel.getInventoryItems()

    fun startInventory() {
        inventoryViewModel.prepareForInventory()
    }

    fun stopInventory() {
        // Implement stop inventory logic in InventoryViewModel if needed
    }
}