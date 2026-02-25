package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class Staging : BaseDataObject() {
    var txId: Int = 0
    var command: Int = 0
    var numProcessed: Int = 0
    var numTotal: Int = 0
    var status: Int = 0
    var remarks: String = ""
    var receiveDate: String = ""
    var processDate: String = ""
    var completeDate: String = ""
    var filename: String = ""
}