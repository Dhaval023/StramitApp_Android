package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class Module : BaseDataObject() {
    var moduleId: Byte = 0
    var moduleName: String = ""
    var modulePrefix: String = ""
}