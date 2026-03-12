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

    // The list of scanned Asset objects — mirrors Xamarin's ScannedItemsList
    private val _scannedAssets = MutableLiveData<List<Asset>>(emptyList())
    val scannedAssets: LiveData<List<Asset>> = _scannedAssets

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _isBusy = MutableLiveData(false)
    val isBusy: LiveData<Boolean> = _isBusy

    // Internal mutable list — same as Xamarin's ObservableCollection<Asset>
    private val internalList = mutableListOf<Asset>()

    // ── Barcode scan (mirrors Xamarin AddBarcodeEvent) ────────────────────────

    fun onBarcodeScanned(barcode: String) {
        if (barcode.isBlank()) return

        viewModelScope.launch {
            try {
                val companyId = AppSettings.tempSelectedSystem?.companyId ?: run {
                    _toastMessage.value = "No company selected. Please select a company first."
                    return@launch
                }

                // 1. Check if barcode already in list (same as Xamarin duplicate check)
                val alreadyInList = internalList.any { it.barcode == barcode }
                if (alreadyInList) {
                    _toastMessage.value = "Barcode already scanned"
                    Log.d("MovementVM", "Barcode already in list: $barcode")
                    return@launch
                }

                // 2. Look up asset in local DB by barcode (mirrors DetermineBarcode)
                val asset = withContext(Dispatchers.IO) {
                    assetDataStore.getItemByBarcodeAsync(companyId, barcode)
                }

                if (asset == null) {
                    _toastMessage.value = "Item not found. Please try again."
                    Log.d("MovementVM", "Asset not found for barcode: $barcode")
                    return@launch
                }

                // 3. Check if asset location has changed (mirrors IsAssetLocationModified)
                val destinationLocationId = AppSettings.tempSelectedLocation?.locationId
                if (asset.locationId == destinationLocationId) {
                    _toastMessage.value = "Asset was not moved. Please change its destination location to proceed."
                    Log.d("MovementVM", "Asset location unchanged, skipping: $barcode")
                    return@launch
                }

                // 4. Add to list
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

    // ── RFID tag scan (mirrors Xamarin AddEvent) ──────────────────────────────

    fun onTagScanned(tagId: String) {
        if (tagId.isBlank()) return

        viewModelScope.launch {
            try {
                val companyId = AppSettings.tempSelectedSystem?.companyId ?: run {
                    _toastMessage.value = "No company selected. Please select a company first."
                    return@launch
                }

                // 1. Check if tag already in list
                val alreadyInList = internalList.any {
                    it.tag?.uppercase() == tagId.uppercase()
                }
                if (alreadyInList) {
                    Log.d("MovementVM", "Tag already in list: $tagId")
                    return@launch
                }

                // 2. Look up asset by tag (mirrors DetermineTag)
                val asset = withContext(Dispatchers.IO) {
                    assetDataStore.getItemByTagAsync(companyId, tagId)
                }

                if (asset == null) {
                    Log.d("MovementVM", "Asset not found for tag: $tagId")
                    return@launch
                }

                // 3. Check if asset location has changed
                val destinationLocationId = AppSettings.tempSelectedLocation?.locationId
                if (asset.locationId == destinationLocationId) {
                    Log.d("MovementVM", "Asset location unchanged, skipping tag: $tagId")
                    return@launch
                }

                // 4. Add to list
                internalList.add(asset)
                _scannedAssets.value = internalList.toList()
                Log.d("MovementVM", "Tag asset added: ${asset.title}, total: ${internalList.size}")

            } catch (e: Exception) {
                Log.e("MovementVM", "Error processing tag: $tagId", e)
            }
        }
    }

    // ── Delete single item (mirrors Xamarin DeleteEvent) ─────────────────────

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

    // ── Clear all (mirrors Xamarin ClearAllEvent) ─────────────────────────────

    fun clearAll() {
        internalList.clear()
        _scannedAssets.value = emptyList()
        _toastMessage.value = "All items cleared"
    }

    fun getCount(): Int = internalList.size
}