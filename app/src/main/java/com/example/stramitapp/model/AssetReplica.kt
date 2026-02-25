package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class AssetReplica : BaseDataObject() {
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
    var status: String = ""
    var costCenterName: String = ""
    var cost: Double = 0.0
    var supplierName: String = ""
    var ownerId: String = ""
    var depreciationDate: String = ""
    var depreciatedValue: Double = 0.0
    var writeOfDate: String = ""
    var writeOfReason: String = ""
    var partId: Int = 0
    var partDesc: String = ""
    var assetTypeId: Int = 0
    var locationId: Int = 0
    var categoryId: Int = 0
    var statusId: Int = 0
    var companyId: Int = 0
    var lastUpdatedBy: Int = 0
    var responsibleUserId: Int = 0
    var longDesc: String = ""
    var gpsLat: Double = 0.0
    var gpsLong: Double = 0.0
    var purchaseDate: String = ""
    var createDate: String = ""
    var createdBy: Int = 0
    var conditionId: Int = 0
    var costCenterId: Int = 0
    var supplierId: Int = 0
    var condition: String = ""
    var assetId: Int = 0
    var deviceId: Int = 0
    var responsibleUserLoginname: String = ""
    var locationLat: Double = 0.0
    var locationLng: Double = 0.0
    var locationAddress: String = ""
    var locationRfidReaderId: String = ""
    var locationTag: String = ""
    var locationBarcode: String = ""
    var updateFlag: String = ""
    var financialId: Int = 0
    var assetValue: Double = 0.0
}