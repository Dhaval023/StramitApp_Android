package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class AssetHistoryDetail : BaseDataObject() {
    var histDetailId: Int = 0
    var histId: Int = 0
    var oldValue: String = ""
    var newValue: String = ""
    var attributeId: Int = 0
    var detailId: Int = 0
    var txType: Int = 0
}