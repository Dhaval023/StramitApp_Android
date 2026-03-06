package com.example.stramitapp.model

import com.example.stramitapp.model.DataObject.BaseDataObject

abstract class Application : BaseDataObject() {
    var appId: Int = 0
    var appName: String = ""
}