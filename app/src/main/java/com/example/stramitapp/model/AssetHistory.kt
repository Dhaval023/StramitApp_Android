package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class AssetHistory : BaseDataObject() {
    var histId: Int = 0
    var assetId: Int = 0
    var deviceId: Int = 0
    var updateDate: String = ""
    var updateBy: Int = 0
}