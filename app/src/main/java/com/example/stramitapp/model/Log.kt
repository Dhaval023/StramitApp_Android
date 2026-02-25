package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class Log : BaseDataObject() {
    var id: Int = 0
    var title: String = ""
    var msg: String = ""
}