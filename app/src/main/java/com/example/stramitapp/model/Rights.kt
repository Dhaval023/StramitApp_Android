package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class Rights : BaseDataObject() {
    var rightsId: Int = 0
    var rightsName: String = ""
    var description: String = ""
    var rightFlag: Boolean = false
}