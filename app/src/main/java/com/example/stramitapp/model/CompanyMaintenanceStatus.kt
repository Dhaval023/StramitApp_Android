package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class CompanyMaintenanceStatus : BaseDataObject() {
    var statusId: Int = 0
    var maintenanceStatus: String = ""
    var companyId: Int = 0
    var updatedBy: Int = 0
    var updateFlag: String = ""
    var lastUpdateDate: String = ""
}