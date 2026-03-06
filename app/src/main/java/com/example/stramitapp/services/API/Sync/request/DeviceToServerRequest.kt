//package com.example.stramitapp.services.API.Sync.request
//
//import com.example.stramitapp.model.Asset
//import com.example.stramitapp.model.AssetFinancialInfo
//import com.example.stramitapp.model.AssetInspectionInfo
//import com.example.stramitapp.model.AssetInsuranceInfo
//import com.example.stramitapp.model.AssetLeaseInfo
//import com.example.stramitapp.model.AssetMaintenanceInfo
//import com.example.stramitapp.model.AssetMemoInfo
//import com.example.stramitapp.model.AssetMovementInfo
//import com.example.stramitapp.model.BillOfMaterial
//import com.google.gson.annotations.SerializedName
//
//data class DeviceToServerRequest(
//
//    @SerializedName("syncVersion")
//    val syncVersion: String? = null,
//
//    @SerializedName("userId")
//    val userId: Int = 0,
//
//    @SerializedName("currentDeviceUdid")
//    val currentDeviceUdid: String? = null,
//
//    @SerializedName("parameters")
//    val parameters: Parameters? = null
//
//) {
//    data class Parameters(
//
//        @SerializedName("tblAssetExtraInfo")
//        val tblAssetExtraInfo: AssetExtraInfoList? = null,
//
//        @SerializedName("tblAssets")
//        val tblAssets: List<Asset>? = null
//    )
//
//    data class AssetExtraInfoList(
//
//        @SerializedName("tblAssetInsuranceInfos")
//        val tblAssetInsuranceInfos: List<AssetInsuranceInfo>? = null,
//
//        @SerializedName("tblAssetLeaseInfos")
//        val tblAssetLeaseInfos: List<AssetLeaseInfo>? = null,
//
//        @SerializedName("tblAssetInspectionInfos")
//        val tblAssetInspectionInfos: List<AssetInspectionInfo>? = null,
//
//        @SerializedName("tblAssetFinancialInfos")
//        val tblAssetFinancialInfos: List<AssetFinancialInfo>? = null,
//
//        @SerializedName("tblAssetMovementInfos")
//        val tblAssetMovementInfos: List<AssetMovementInfo>? = null,
//
//        @SerializedName("tblAssetMemoInfos")
//        val tblAssetMemoInfos: List<AssetMemoInfo>? = null,
//
//        @SerializedName("tblAssetMaintenanceInfos")
//        val tblAssetMaintenanceInfos: List<AssetMaintenanceInfo>? = null,
//
//        @SerializedName("tblAssetBOMInfos")
//        val tblAssetBOMInfos: List<BillOfMaterial>? = null
//    )
//}

package com.example.stramitapp.services.API.Sync.request

import com.example.stramitapp.model.*

data class DeviceToServerRequest(
    var syncVersion: String? = null,
    var userId: Int = 0,
    var currentDeviceUdid: String? = null,
    var parameters: Parameters? = null
) {

    data class Parameters(
        var tblAssetExtraInfo: AssetExtraInfoList? = null,
        var tblAssets: List<Asset>? = null
    )

    data class AssetExtraInfoList(
        var tblAssetInsuranceInfos: List<AssetInsuranceInfo>? = null,
        var tblAssetLeaseInfos: List<AssetLeaseInfo>? = null,
        var tblAssetInspectionInfos: List<AssetInspectionInfo>? = null,
        var tblAssetFinancialInfos: List<AssetFinancialInfo>? = null,
        var tblAssetMovementInfos: List<AssetMovementInfo>? = null,
        var tblAssetMemoInfos: List<AssetMemoInfo>? = null,
        var tblAssetMaintenanceInfos: List<AssetMaintenanceInfo>? = null,
        var tblAssetBOMInfos: List<BillOfMaterial>? = null
    )
}