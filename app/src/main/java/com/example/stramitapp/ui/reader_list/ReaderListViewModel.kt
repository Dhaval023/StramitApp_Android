package com.example.stramitapp.ui.reader_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stramitapp.zebraconnection.RFIDHandler
import com.zebra.rfid.api3.ReaderDevice

class ReaderListViewModel : ViewModel() {

    private val _readerList = MutableLiveData<List<ReaderDevice>>()
    val readerList: LiveData<List<ReaderDevice>> = _readerList

    private val _connectedReaderName = MutableLiveData<String?>()
    val connectedReaderName: LiveData<String?> = _connectedReaderName

    fun getAvailableReaders(rfidHandler: RFIDHandler?) {
        rfidHandler?.let {
            try {
                RFIDHandler.tryGetAvailableReaders()
                _readerList.postValue(RFIDHandler.availableRFIDReaderList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun connectToReader(rfidHandler: RFIDHandler?, readerDevice: ReaderDevice) {
        if (_connectedReaderName.value == readerDevice.name && rfidHandler?.connectionStatus?.value == true) {
            // Already connected to this reader, don't reconnect
            return
        }

        rfidHandler?.let {
            it.selectReader(readerDevice)
            it.connect(readerDevice.name)
            _connectedReaderName.postValue(readerDevice.name)
        }
    }

    fun updateConnectedReader(name: String?) {
        _connectedReaderName.postValue(name)
    }
}