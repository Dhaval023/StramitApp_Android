package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class AssetIssueReturnInfo : BaseDataObject() {
    var id: Int = 0
    var attributeDeviceId: Int = 0
    var assetId: Int = 0
    var deviceId: Int = 0
    var irType: String = ""
    var irDate: String = ""
    var issueTo: Int = 0
    var issueToSupplierId: Int = 0
    var comment: String = ""
    var conditionId: Int = 0
    var signature: String = ""
    var personName: String = ""
    var updateFlag: String = ""
    var lastUpdateDate: String = ""
    var sigLastUpdateDate: String = ""
    var updatedBy: Int = 0
}