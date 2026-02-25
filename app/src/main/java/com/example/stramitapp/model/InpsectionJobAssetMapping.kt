package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class InspectionJobAssetMapping : BaseDataObject() {
    var assetId: Int = 0
    var deviceId: Int = 0
    var jobId: Int = 0
    var lastUpdatedBy: Int = 0
    var lastUpdatedDate: String = ""
}