package com.example.stramitapp.ui.sync

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stramitapp.services.SyncService
import com.example.stramitapp.utilities.SecurePrefs
import com.example.stramitapp.models.Constants.StorageKeys
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SyncViewModel : ViewModel() {

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

            _isSyncing.value = true

            try {
                val syncService = SyncService()
                val result = if (force) {
                    syncService.forceSync()
                } else {
                    syncService.sync()
                }

                if (result) {
                    setDateTime()
                }

                onResult(result)

            } catch (e: Exception) {
                onResult(false)
            }

            _isSyncing.value = false
        }
    }
    private fun setDateTime() {
        val lastSyncStr = SecurePrefs.get(StorageKeys.LastSyncDataStorageKey)

        if (!lastSyncStr.isNullOrEmpty()) {
            var date: Date? = null

            val timestamp = lastSyncStr.toLongOrNull()
            if (timestamp != null) {
                date = Date(timestamp)
            } else {
                try {
                    val sqliteFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)
                    sqliteFormat.timeZone = TimeZone.getTimeZone("UTC")
                    date = sqliteFormat.parse(lastSyncStr)
                } catch (e: Exception) {
                }
            }

            if (date != null) {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                
                dateFormat.timeZone = TimeZone.getDefault()
                timeFormat.timeZone = TimeZone.getDefault()

                _date.value = dateFormat.format(date)
                _time.value = timeFormat.format(date)
                return
            }
        }
        _date.value = "N/A"
        _time.value = "N/A"
    }
}