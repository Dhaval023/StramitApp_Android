package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class Role : BaseDataObject() {
    var roleId: Int = 0
    var roleName: String = ""
    var createdBy: Int = 0
    var description: String = ""
    var isActive: Int = 0
    var roleType: Int = 0
}