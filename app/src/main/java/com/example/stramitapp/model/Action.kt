package com.example.stramitapp.model

import com.example.stramitapp.model.DataObject.BaseDataObject

class Action : BaseDataObject() {
    var actionId: Int = 0
    var readerId: Int = 0
    var seq: Int = 0
    var actionCode: String = ""
    var param: String = ""
}