package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class CompanyLocationTree : BaseDataObject() {
    var locationId: Int = 0
    var locationDepth: Int = 0
    var locationPath: String = ""
    var fullLocationName: String = ""
}