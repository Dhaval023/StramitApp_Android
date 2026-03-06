package com.example.stramitapp.model

import com.example.stramitapp.model.DataObject.BaseDataObject

class LicenseePeer : BaseDataObject() {
    override var id: Int = 0
    var licenseeId: Int = 0
    var userId: Int = 0
}