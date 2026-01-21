package com.example.stramitapp.ui.reader_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stramitapp.zebraconnection.RFIDHandler
import com.zebra.rfid.api3.ReaderDevice

class ReaderListViewModel : ViewModel() {

    private val _readerList = MutableLiveData<List<ReaderDevice>>()
    val readerList: LiveData<List<ReaderDevice>> = _readerList

    fun getAvailableReaders(rfidHandler: RFIDHandler?) {
        rfidHandler?.let {
            RFIDHandler.tryGetAvailableReaders()
            _readerList.postValue(RFIDHandler.availableRFIDReaderList)
        }
    }

    fun connectToReader(rfidHandler: RFIDHandler?, readerDevice: ReaderDevice) {
        rfidHandler?.selectReader(readerDevice)
        rfidHandler?.connect(readerDevice.name)
    }
}