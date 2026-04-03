package com.example.stramitapp.services


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stramitapp.model.DataObject.ItemDetailsModel
import kotlinx.coroutines.launch

class ItemViewModel : ViewModel() {

    private val messageObj = Any()

    var orderValue: Int = 0
        set(value) {
            field = value
            order = "$value."
        }

    var order: String = "0"

    var orderIsVisible: Boolean = false
    var detailsIsVisible: Boolean = false
    var deleteButtonIsVisible: Boolean = false
    var deleteButtonIsEnabled: Boolean = true
    var openButtonIsVisible: Boolean = false
    var openButtonIsEnabled: Boolean = true
    var openButtonAuxIsVisible: Boolean = false
    var openButtonAuxIsEnabled: Boolean = true
    var openButtonScanIsVisible: Boolean = false
    var openButtonScanIsEnabled: Boolean = true
    var imagemAuxIsVisible: Boolean = false
    var imagemAuxIsEnabled: Boolean = true
    var sourceImageAux: String = ""
    var sourceImagemButtonAux: String = ""

    var isLoading: Boolean = false

    var model: ItemDetailsModel? = null

    var isBusy: Boolean = false

    var title: String = "Browse"

    fun deleteCommand() {
        viewModelScope.launch {
            executeDeleteCommand()
        }
    }

    fun openCommand() {
        viewModelScope.launch {
            executeOpenCommand()
        }
    }

    private suspend fun executeDeleteCommand() {
        if (isBusy) return

        isBusy = true

        try {
            val items = model?.value

        } catch (ex: Exception) {
            Log.e("ItemViewModel", "Delete Error", ex)
            showError("Unable to delete item.")

        } finally {
            isBusy = false
        }
    }
    private suspend fun executeOpenCommand() {
        if (isBusy) return

        isBusy = true

        try {
            val item = model?.value

        } catch (ex: Exception) {
            Log.e("ItemViewModel", "Open Error", ex)

            showError("Unable to open the item.")

        } finally {
            isBusy = false
        }
    }
    private fun showError(message: String) {
        Log.e("ItemViewModel", message)
    }
}