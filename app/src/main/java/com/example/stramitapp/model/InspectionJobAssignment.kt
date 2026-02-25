package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class InspectionJobAssignment : BaseDataObject() {
    var jobId: Int = 0
    var assignTo: Int = 0
    var deviceUdid: String = ""
    var status: Int = 0
    var updateFlag: String = ""
    var lastUpdateDate: String = ""
}