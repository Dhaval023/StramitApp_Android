package com.example.stramitapp.ui.floorsweep

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stramitapp.model.FloorSweepResultListModel
import java.text.SimpleDateFormat
import java.util.*

class FloorSweepResultViewModel : ViewModel() {

    private val _floorSweepResultList = MutableLiveData<List<FloorSweepResultListModel>>()
    val floorSweepResultList: LiveData<List<FloorSweepResultListModel>> = _floorSweepResultList

    private val _isBusy = MutableLiveData<Boolean>(false)
    val isBusy: LiveData<Boolean> = _isBusy
    private val _navigateToLocate = MutableLiveData<Pair<String, String>?>()
    val navigateToLocate: LiveData<Pair<String, String>?> = _navigateToLocate
    private val _navigateToHome = MutableLiveData<Boolean>(false)
    val navigateToHome: LiveData<Boolean> = _navigateToHome

    fun setResults(results: List<FloorSweepResultListModel>) {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sortedList = results.sortedBy { 
            try {
                sdf.parse(it.deliveryDate)
            } catch (e: Exception) {
                Date(0)
            }
        }
        _floorSweepResultList.value = sortedList
    }

    fun selectResultEvent(selectedItem: FloorSweepResultListModel) {
        _navigateToLocate.value = Pair(selectedItem.rfid, selectedItem.id)
    }

    fun closePageEvent() {
        _navigateToHome.value = true
    }

    fun onNavigationHandled() {
        _navigateToLocate.value = null
        _navigateToHome.value = false
    }
}
