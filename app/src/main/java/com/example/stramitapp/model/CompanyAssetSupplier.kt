package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class CompanyAssetSupplier : BaseDataObject() {
    var supplierId: Int = 0
    var supplierName: String = ""
    var companyId: Int = 0
    var updateFlag: String = ""
    var lastUpdateDate: String = ""
    var updatedBy: Int = 0
    var issueToSupplierId: Int = 0
}




