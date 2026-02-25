package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class ReaderTxn : BaseDataObject() {
    var id: Int = 0
    var readerName: String = ""
    var macAddress: String = ""
    var epc: String = ""
    var tid: String = ""
    var antennaPort: String = ""
    var peakRssi: Int = 0
    var firstSeenDate: String = ""
    var receiveDate: String = ""
    var remarks: String = ""
    var assetId: Int = 0
    var deviceId: Int = 0
}