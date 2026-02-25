package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class AssetIssueReturnInfoImageMapping : BaseDataObject() {
    var id: Int = 0
    var attributeDeviceId: Int = 0
    var assetIssueInfoId: Int = 0
    var airAttributeDeviceId: Int = 0
    var image: String = ""
    var flag: Int = 0
    var lastUpdateDate: String = ""
}