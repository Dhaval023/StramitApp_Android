package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class AssetInspectionJobTrackInfo : BaseDataObject() {
    var jobId: Int = 0
    var checklistItemId: Int = 0
    var assetId: Int = 0
    var deviceId: Int = 0
    var locationId: Int = 0
    var image: String = ""
    var comments: String = ""
    var inspectedBy: Int = 0
    var inspectedDate: Int = 0
    var lastUpdateDate: String = ""
    var updateFlag: String = ""
    var checklistItemValue: Int = 0
}