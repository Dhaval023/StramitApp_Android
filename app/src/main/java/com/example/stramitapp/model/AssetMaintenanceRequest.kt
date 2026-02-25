package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class AssetMaintenanceRequest : BaseDataObject() {
    var id: Int = 0
    var assetId: Int = 0
    var deviceId: Int = 0
    var requestDate: String = ""
    var requestBy: Int = 0
    var comments: String = ""
    var memo: String = ""
    var maintenanceStatus: Int = 0
    var statusUpdateBy: Int = 0
    var statusUpdateDate: Int = 0
    var updateFlag: String = ""
    var lastUpdateDate: String = ""
    var memoLastUpdateDate: String = ""
}