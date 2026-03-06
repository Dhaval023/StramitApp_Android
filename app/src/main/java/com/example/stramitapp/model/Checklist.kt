package com.example.stramitapp.model

import com.example.stramitapp.model.DataObject.BaseDataObject

abstract class Checklist : BaseDataObject() {
    var checklistId: Int = 0
    var checklistName: String = ""
    var description: String = ""
    var partId: Int = 0
    var companyId: Int = 0
    var duration: Int = 0
    var createdByUserId: Int = 0
    var updatedBy: Int = 0
    var lastUpdateDate: String = ""
    var updateFlag: String = ""
}