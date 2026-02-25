package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class InspectionJobDefinition : BaseDataObject() {
    var jobId: Int = 0
    var jobName: String = ""
    var companyId: Int = 0
    var creationDate: String = ""
    var createdBy: Int = 0
    var status: Int = 0
    var submittionDate: String = ""
    var locationId: Int = 0
    var jobDesc: String = ""
    var isAllocated: Boolean = false
    var updateFlag: String = ""
    var lastUpdateDate: String = ""
}