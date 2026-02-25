package com.example.stramitapp.models

import java.time.OffsetDateTime

class SimpleDeviceToServerRequest {
    var userId: Int = 0
    var currentDeviceUdid: String? = null
    var parameters: RootObject? = null
}

class RootObject {
    var tblAssetExtraInfo: TblAssetExtraInfo? = null
    var tblAssets: List<TblAsset>? = null
    var mobileJobList: List<MobileJobList>? = null
}

class TblAssetExtraInfo {
    var tblAssetMovementInfos: List<TblAssetMovementInfo>? = null
}

class TblAssetMovementInfo {
    var id: String? = null
    var movementRecordedBy: String? = null
    var movedBy: String? = null
    var movementType: String? = null
    var movementDateStr: String? = null
    var movementDate: OffsetDateTime? = null
    var updateFlag: String? = null
    var assetId: String? = null
    var deviceId: String? = null
    var updatedBy: String? = null
    var attributeDeviceId: Int = 0
    var sourceLocationId: String? = null
    var destinationLocationId: String? = null
    var workOrderNumber: String? = null
}

class TblAsset {
    var assetId: Int = 0
    var deviceId: Int = 0
    var lastUpdateDeviceId: Int = 0
    var companyAssetId: String? = null
    var assetType: Int? = null
    var title: String? = null
    var tag: String? = null
    var barcode: String? = null
    var serialNumber: String? = null
    var assetValue: Double = 0.0
    var weight: Double = 0.0
    var partId: Int = 0
    var subPartId: Int = 0
    var locationId: Int = 0
    var categoryId: Int = 0
    var statusId: Int = 0
    var companyId: Int = 0
    var conditionId: Int = 0
    var longDesc: String? = null
    var gpsLat: Float? = null
    var gpsLong: Float? = null
    var assetImage: String? = null
    var createDate: OffsetDateTime? = null
    var purchaseDate: OffsetDateTime? = null
    var lastUpdateDate: OffsetDateTime? = null
    var createdBy: Int? = null
    var responsibleUserId: Int = 0
    var lastUpdatedBy: Int = 0
    var updateFlag: String? = null
    var dTSMMSync: String? = null
    var sTDMMSync: String? = null
    var flagSync: Int = 0
    var imageSync: Int = 0
    var length: String? = null
    var width: String? = null
    var girth: String? = null
    var colour: String? = null
    var numberOfBends: String? = null
    var packNumber: String? = null
    var totalPackNumber: String? = null
    var splitNumber: String? = null
    var supplierReference: String? = null
    var supplierNumber: String? = null
    var docketNumber: String? = null
    var purchaseOrderNumber: String? = null
    var erpOrderNumber: String? = null
    var customerName: String? = null
    var customerReference: String? = null
    var deliveryNumber: String? = null
    var dropNumber: String? = null
    var shipmentNumber: String? = null
    var route: String? = null
    var deliveryInstruction: String? = null
    var address: String? = null
    var distributionOrderNumber: String? = null
    var quantity: String? = null
    var quantityUOM: String? = null
    var lengthUOM: String? = null
    var heightUOM: String? = null
    var widthUOM: String? = null
    var weightUOM: String? = null
    var girthUOM: String? = null
    var colourUOM: String? = null
    var packageStructure: String? = null
    var manufacturingInstruction: String? = null
    var scheduleNumber: String? = null
    var markNumber: String? = null
    var packDescription: String? = null
    var packageNumber: String? = null
}

class MobileJobList {
    var wpCompanyId: String? = null
    var companyId: String? = null
    var locationId: String? = null
    var jobTypeId: String? = null
    var jobDescId: String? = null
    var barcode: String? = null
    var userId: String? = null
    var jobNumber: String? = null
    var createDate: OffsetDateTime? = null
    var lastUpdatedUser: String? = null
}