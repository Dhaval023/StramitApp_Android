package com.example.stramitapp.model

import com.example.stramitapp.model.DataObject.BaseDataObject

class AssetFinancialInfoDisposeImageMapping : BaseDataObject() {
  override  var id: Int = 0
    var financialInfoId: Int = 0
    var image: String = ""
    var flag: Int = 0
    var lastUpdateDate: String = ""
}