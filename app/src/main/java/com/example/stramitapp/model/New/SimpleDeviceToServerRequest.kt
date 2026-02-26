package com.example.stramitapp.model.New

import com.google.gson.annotations.SerializedName
import java.util.Date

data class SimpleDeviceToServerRequest(

    @SerializedName("userId")
    val userId: Int = 0,

    @SerializedName("currentDeviceUdid")
    val currentDeviceUdid: String? = null,

    @SerializedName("parameters")
    val parameters: RootObject? = null
)

data class RootObject(

    @SerializedName("tblAssetExtraInfo")
    val tblAssetExtraInfo: TblAssetExtraInfo? = null,

    @SerializedName("tblAssets")
    val tblAssets: List<TblAsset>? = null,

    @SerializedName("mobileJobList")
    val mobileJobList: List<MobileJobList>? = null
)

data class TblAssetExtraInfo(

    @SerializedName("tblAssetMovementInfos")
    val tblAssetMovementInfos: List<TblAssetMovementInfo>? = null
)

data class TblAssetMovementInfo(

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("movementRecordedBy")
    val movementRecordedBy: String? = null,

    @SerializedName("movedBy")
    val movedBy: String? = null,

    @SerializedName("movementType")
    val movementType: String? = null,

    @SerializedName("movementDateStr")
    val movementDateStr: String? = null,

    @SerializedName("movementDate")
    val movementDate: Date? = null,

    @SerializedName("updateFlag")
    val updateFlag: String? = null,

    @SerializedName("assetId")
    val assetId: String? = null,

    @SerializedName("deviceId")
    val deviceId: String? = null,

    @SerializedName("updatedBy")
    val updatedBy: String? = null,

    @SerializedName("attributeDeviceId")
    val attributeDeviceId: Int = 0,

    @SerializedName("sourceLocationId")
    val sourceLocationId: String? = null,

    @SerializedName("destinationLocationId")
    val destinationLocationId: String? = null,

    @SerializedName("workOrderNumber")
    val workOrderNumber: String? = null
)

data class TblAsset(

    @SerializedName("assetId")
    val assetId: Int = 0,

    @SerializedName("deviceId")
    val deviceId: Int = 0,

    @SerializedName("lastUpdateDeviceId")
    val lastUpdateDeviceId: Int = 0,

    @SerializedName("companyAssetId")
    val companyAssetId: String? = null,

    @SerializedName("assetType")
    val assetType: Int? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("tag")
    val tag: String? = null,

    @SerializedName("barcode")
    val barcode: String? = null,

    @SerializedName("serialNumber")
    val serialNumber: String? = null,

    @SerializedName("assetValue")
    val assetValue: Double = 0.0,

    @SerializedName("weight")
    val weight: Double = 0.0,

    @SerializedName("partId")
    val partId: Int = 0,

    @SerializedName("subPartId")
    val subPartId: Int = 0,

    @SerializedName("locationId")
    val locationId: Int = 0,

    @SerializedName("categoryId")
    val categoryId: Int = 0,

    @SerializedName("statusId")
    val statusId: Int = 0,

    @SerializedName("companyId")
    val companyId: Int = 0,

    @SerializedName("conditionId")
    val conditionId: Int = 0,

    @SerializedName("longDesc")
    val longDesc: String? = null,

    @SerializedName("gpsLat")
    val gpsLat: Float? = null,

    @SerializedName("gpsLong")
    val gpsLong: Float? = null,

    @SerializedName("assetImage")
    val assetImage: String? = null,

    @SerializedName("createDate")
    val createDate: Date? = null,

    @SerializedName("purchaseDate")
    val purchaseDate: Date? = null,

    @SerializedName("lastUpdateDate")
    val lastUpdateDate: Date? = null,

    @SerializedName("createdBy")
    val createdBy: Int? = null,

    @SerializedName("responsibleUserId")
    val responsibleUserId: Int = 0,

    @SerializedName("lastUpdatedBy")
    val lastUpdatedBy: Int = 0,

    @SerializedName("updateFlag")
    val updateFlag: String? = null,

    @SerializedName("dTSMMSync")
    val dTSMMSync: String? = null,

    @SerializedName("sTDMMSync")
    val sTDMMSync: String? = null,

    @SerializedName("flagSync")
    val flagSync: Int = 0,

    @SerializedName("imageSync")
    val imageSync: Int = 0,

    @SerializedName("lenght")
    val lenght: String? = null,

    @SerializedName("width")
    val width: String? = null,

    @SerializedName("girth")
    val girth: String? = null,

    @SerializedName("colour")
    val colour: String? = null,

    @SerializedName("numberOfBends")
    val numberOfBends: String? = null,

    @SerializedName("packNumber")
    val packNumber: String? = null,

    @SerializedName("totalPackNumber")
    val totalPackNumber: String? = null,

    @SerializedName("splitNumber")
    val splitNumber: String? = null,

    @SerializedName("supplierreference")
    val supplierreference: String? = null,

    @SerializedName("suppliernumber")
    val suppliernumber: String? = null,

    @SerializedName("docketNumber")
    val docketNumber: String? = null,

    @SerializedName("purchaseOrderNumber")
    val purchaseOrderNumber: String? = null,

    @SerializedName("eroOrderNumber")
    val eroOrderNumber: String? = null,

    @SerializedName("customername")
    val customername: String? = null,

    @SerializedName("customerReference")
    val customerReference: String? = null,

    @SerializedName("deliveryNumber")
    val deliveryNumber: String? = null,

    @SerializedName("dropNumber")
    val dropNumber: String? = null,

    @SerializedName("shipmentNumber")
    val shipmentNumber: String? = null,

    @SerializedName("route")
    val route: String? = null,

    @SerializedName("deliveryInstruction")
    val deliveryInstruction: String? = null,

    @SerializedName("address")
    val address: String? = null,

    @SerializedName("distributionOrderNumber")
    val distributionOrderNumber: String? = null,

    @SerializedName("quantity")
    val quantity: String? = null,

    @SerializedName("quantityUOM")
    val quantityUOM: String? = null,

    @SerializedName("lenghtUOM")
    val lenghtUOM: String? = null,

    @SerializedName("heightUOM")
    val heightUOM: String? = null,

    @SerializedName("widthUOM")
    val widthUOM: String? = null,

    @SerializedName("weightUOM")
    val weightUOM: String? = null,

    @SerializedName("girthUOM")
    val girthUOM: String? = null,

    @SerializedName("colourUOM")
    val colourUOM: String? = null,

    @SerializedName("packageStructure")
    val packageStructure: String? = null,

    @SerializedName("manufacturingIstruction")
    val manufacturingIstruction: String? = null,

    @SerializedName("scheduleNumber")
    val scheduleNumber: String? = null,

    @SerializedName("markNumber")
    val markNumber: String? = null,

    @SerializedName("packDescription")
    val packDescription: String? = null,

    @SerializedName("packageNumber")
    val packageNumber: String? = null
)

data class MobileJobList(

    @SerializedName("wpCompanyId")
    val wpCompanyId: String? = null,

    @SerializedName("companyId")
    val companyId: String? = null,

    @SerializedName("locationId")
    val locationId: String? = null,

    @SerializedName("jobTypeId")
    val jobTypeId: String? = null,

    @SerializedName("jobDescId")
    val jobDescId: String? = null,

    @SerializedName("barcode")
    val barcode: String? = null,

    @SerializedName("userId")
    val userId: String? = null,

    @SerializedName("jobNumber")
    val jobNumber: String? = null,

    @SerializedName("createDate")
    val createDate: Date? = null,

    @SerializedName("lastUpdatedUser")
    val lastUpdatedUser: String? = null
)