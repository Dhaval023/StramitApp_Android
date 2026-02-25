package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class Reader : BaseDataObject() {
    var id: Int = 0
    var name: String = ""
    var macAddress: String = ""
    var description: String = ""
    var companyId: Int = 0
    var userId: Int = 0
    var lastUpdateDate: String = ""
    var updatedBy: Int = 0
    var readKey: String = ""
    var assetKey: String = ""
}