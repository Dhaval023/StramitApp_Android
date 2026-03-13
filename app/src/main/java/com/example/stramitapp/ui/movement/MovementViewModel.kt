package com.example.stramitapp.ui.movement

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stramitapp.model.Asset
import com.example.stramitapp.Repositories.AssetDataStore
import com.example.stramitapp.utilities.AppSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovementViewModel : ViewModel() {

    private val assetDataStore = AssetDataStore()
    private val _scannedAssets = MutableLiveData<List<Asset>>(emptyList())
    val scannedAssets: LiveData<List<Asset>> = _scannedAssets

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _isBusy = MutableLiveData(false)
    val isBusy: LiveData<Boolean> = _isBusy
    private val internalList = mutableListOf<Asset>()

    fun onBarcodeScanned(barcode: String) {
        if (barcode.isBlank()) return

        viewModelScope.launch {
            try {
                val companyId = AppSettings.tempSelectedSystem?.companyId ?: run {
                    _toastMessage.value = "No company selected. Please select a company first."
                    return@launch
                }

                val alreadyInList = internalList.any { it.barcode == barcode }
                if (alreadyInList) {
                    _toastMessage.value = "Barcode already scanned"
                    Log.d("MovementVM", "Barcode already in list: $barcode")
                    return@launch
                }

                val asset = withContext(Dispatchers.IO) {
                    assetDataStore.getItemByBarcodeAsync(companyId, barcode)
                }

                if (asset == null) {
                    _toastMessage.value = "Item not found. Please try again."
                    Log.d("MovementVM", "Asset not found for barcode: $barcode")
                    return@launch
                }

                val destinationLocationId = AppSettings.tempSelectedLocation?.locationId
                if (asset.locationId == destinationLocationId) {
                    _toastMessage.value = "Asset was not moved. Please change its destination location to proceed."
                    Log.d("MovementVM", "Asset location unchanged, skipping: $barcode")
                    return@launch
                }

                internalList.add(asset)
                _scannedAssets.value = internalList.toList()
                _toastMessage.value = "Asset added: ${asset.title ?: barcode}"
                Log.d("MovementVM", "Asset added: ${asset.title}, total: ${internalList.size}")

            } catch (e: Exception) {
                Log.e("MovementVM", "Error processing barcode: $barcode", e)
                _toastMessage.value = "Error processing barcode. Please try again."
            }
        }
    }

    fun onTagScanned(tagId: String) {
        if (tagId.isBlank()) return

        viewModelScope.launch {
            try {
                val companyId = AppSettings.tempSelectedSystem?.companyId ?: run {
                    _toastMessage.value = "No company selected. Please select a company first."
                    return@launch
                }

                val alreadyInList = internalList.any {
                    it.tag?.uppercase() == tagId.uppercase()
                }
                if (alreadyInList) {
                    Log.d("MovementVM", "Tag already in list: $tagId")
                    return@launch
                }

                val asset = withContext(Dispatchers.IO) {
                    assetDataStore.getItemByTagAsync(companyId, tagId)
                }

                if (asset == null) {
                    Log.d("MovementVM", "Asset not found for tag: $tagId")
                    return@launch
                }

                val destinationLocationId = AppSettings.tempSelectedLocation?.locationId
                if (asset.locationId == destinationLocationId) {
                    Log.d("MovementVM", "Asset location unchanged, skipping tag: $tagId")
                    return@launch
                }

                internalList.add(asset)
                _scannedAssets.value = internalList.toList()
                Log.d("MovementVM", "Tag asset added: ${asset.title}, total: ${internalList.size}")

            } catch (e: Exception) {
                Log.e("MovementVM", "Error processing tag: $tagId", e)
            }
        }
    }

    fun submitMovement() {
        val assets = scannedAssets.value ?: return
        // TODO: call your API / repository here, e.g.:

        clearAll()
        _toastMessage.value = "Movement submitted successfully"
    }
    fun deleteItem(asset: Asset) {
        internalList.remove(asset)
        _scannedAssets.value = internalList.toList()
        _toastMessage.value = "Item deleted"
    }

    fun deleteItemAt(position: Int) {
        if (position in internalList.indices) {
            internalList.removeAt(position)
            _scannedAssets.value = internalList.toList()
            _toastMessage.value = "Item deleted"
        }
    }

    fun clearAll() {
        internalList.clear()
        _scannedAssets.value = emptyList()
        _toastMessage.value = "All items cleared"
    }

    fun getCount(): Int = internalList.size
}