package com.example.stramitapp.ui.reader_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stramitapp.Global
import com.example.stramitapp.zebraconnection.RFIDHandler
import com.zebra.rfid.api3.ReaderDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReaderListViewModel : ViewModel() {

    private val _readerList = MutableLiveData<List<ReaderDevice>>()
    val readerList: LiveData<List<ReaderDevice>> = _readerList
    private val _connectedReaderName = MutableLiveData<String?>()
    val connectedReaderName: LiveData<String?> = _connectedReaderName
    private val _isScanning = MutableLiveData<Boolean>(false)
    val isScanning: LiveData<Boolean> = _isScanning

    fun getAvailableReaders(rfidHandler: RFIDHandler?) {
        if (_isScanning.value == true) return

        _isScanning.value = true
        viewModelScope.launch(Dispatchers.IO) {
            rfidHandler?.let {
                try {
                    RFIDHandler.tryGetAvailableReaders()
                    _readerList.postValue(RFIDHandler.availableRFIDReaderList ?: emptyList())
                    checkCurrentConnection(rfidHandler)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    _isScanning.postValue(false)
                }
            } ?: run {
                _isScanning.postValue(false)
            }
        }
    }

    fun checkCurrentConnection(rfidHandler: RFIDHandler?) {
        val connectedReader = RFIDHandler.mConnectedRfidReader
        if (connectedReader != null && connectedReader.isConnected) {
            _connectedReaderName.postValue(connectedReader.hostName)
        } else {
            _connectedReaderName.postValue(null)
        }
    }

    fun connectToReader(rfidHandler: RFIDHandler?, readerDevice: ReaderDevice) {
        if (_connectedReaderName.value == readerDevice.name &&
            RFIDHandler.mConnectedRfidReader?.isConnected == true) {
            return
        }

        rfidHandler?.let {
            viewModelScope.launch(Dispatchers.IO) {
                if (RFIDHandler.mConnectedRfidReader?.isConnected == true) {
                    it.disconnect()
                }

                it.selectReader(readerDevice)
                it.connect(readerDevice.name)
                _connectedReaderName.postValue(readerDevice.name)
                Global.setRfidMode()
            }
        }
    }

    fun disconnectReader(rfidHandler: RFIDHandler?) {
        viewModelScope.launch(Dispatchers.IO) {
            rfidHandler?.disconnect()
            _connectedReaderName.postValue(null)
        }
    }

    fun updateConnectedReader(name: String?) {
        _connectedReaderName.postValue(name)
    }
}