package com.example.stramitapp.ui.sync

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stramitapp.models.Database.AppDatabase
import com.example.stramitapp.services.SyncService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SyncViewModel : ViewModel() {

    private val db = AppDatabase.getInstance()

    private val syncService = SyncService()

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _time = MutableLiveData<String>()
    val time: LiveData<String> = _time

    private val _isSyncing = MutableLiveData<Boolean>(false)
    val isSyncing: LiveData<Boolean> = _isSyncing

    init {
        setDateTime()
    }

    fun startSync(force: Boolean, onResult: (Boolean) -> Unit) {

        viewModelScope.launch {

            _isSyncing.postValue(true)

            try {

                val syncService = SyncService()

                val result = if (force) {
                    syncService.forceSync()
                } else {
                    syncService.sync()
                }

                onResult(result)

            } catch (e: Exception) {
                onResult(false)
            }

            _isSyncing.postValue(false)
        }
    }

    private fun setDateTime() {

        val now = Date()

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        _date.value = dateFormat.format(now)
        _time.value = timeFormat.format(now)
    }
}