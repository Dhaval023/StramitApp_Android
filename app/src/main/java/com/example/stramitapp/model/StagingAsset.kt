package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class StagingAsset : BaseDataObject() {
    var id: Int = 0
    var txId: Int = 0
    var userId: Int = 0
    var companyAssetId: String = ""
    var title: String = ""
    var tag: String = ""
    var barcode: String = ""
    var serialNumber: String = ""
    var partName: String = ""
    var assetType: String = ""
    var locationName: String = ""
    var categoryName: String = ""
    var statusName: String = ""
    var costCenterName: String = ""
    var cost: Double = 0.0
    var supplierName: String = ""
    var ownerId: String = ""
    var depreciationDate: String = ""
    var depreciatedValue: Double = 0.0
    var writeOffDate: String = ""
    var writeOffReason: String = ""
    var partDesc: String = ""
    var longDesc: String = ""
    var gpsLat: Double = 0.0
    var gpsLong: Double = 0.0
    var purchaseDate: String = ""
    var condition: String = ""
    var responsibleUserLoginname: String = ""
    var locationLat: Double = 0.0
    var locationLng: Double = 0.0
    var locationAddress: String = ""
    var locationRfidReaderId: String = ""
    var locationTag: String = ""
    var locationBarcode: String = ""
    var assetValue: Double = 0.0
    var rowNumber: Int = 0
    var companyId: Int = 0
    var mergeBehaviour: Int = 0
    var updateBy: Int = 0
    var licenseeUserId: Int = 0
    var weight: Double = 0.0
}