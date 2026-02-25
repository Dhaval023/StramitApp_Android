package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class CompanyCostCenter : BaseDataObject() {
    var costCenterId: Int = 0
    var costCenterName: String = ""
    var costCenterDescription: String = ""
    var companyId: Int = 0
    var updateFlag: String = ""
    var lastUpdateDate: String = ""
    var updatedBy: Int = 0
}