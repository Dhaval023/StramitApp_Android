package com.example.stramitapp.services.API.Sync.request

import com.example.stramitapp.models.Asset
import com.example.stramitapp.models.AssetFinancialInfo
import com.example.stramitapp.models.AssetInspectionInfo
import com.example.stramitapp.models.AssetInsuranceInfo
import com.example.stramitapp.models.AssetLeaseInfo
import com.example.stramitapp.models.AssetMaintenanceInfo
import com.example.stramitapp.models.AssetMemoInfo
import com.example.stramitapp.models.AssetMovementInfo
import com.example.stramitapp.models.BillOfMaterial
import com.google.gson.annotations.SerializedName

data class DeviceToServerRequest(

    @SerializedName("syncVersion")
    val syncVersion: String? = null,

    @SerializedName("userId")
    val userId: Int = 0,

    @SerializedName("currentDeviceUdid")
    val currentDeviceUdid: String? = null,

    @SerializedName("parameters")
    val parameters: Parameters? = null

) {
    data class Parameters(

        @SerializedName("tblAssetExtraInfo")
        val tblAssetExtraInfo: AssetExtraInfoList? = null,

        @SerializedName("tblAssets")
        val tblAssets: List<Asset>? = null
    )

    data class AssetExtraInfoList(

        @SerializedName("tblAssetInsuranceInfos")
        val tblAssetInsuranceInfos: List<AssetInsuranceInfo>? = null,

        @SerializedName("tblAssetLeaseInfos")
        val tblAssetLeaseInfos: List<AssetLeaseInfo>? = null,

        @SerializedName("tblAssetInspectionInfos")
        val tblAssetInspectionInfos: List<AssetInspectionInfo>? = null,

        @SerializedName("tblAssetFinancialInfos")
        val tblAssetFinancialInfos: List<AssetFinancialInfo>? = null,

        @SerializedName("tblAssetMovementInfos")
        val tblAssetMovementInfos: List<AssetMovementInfo>? = null,

        @SerializedName("tblAssetMemoInfos")
        val tblAssetMemoInfos: List<AssetMemoInfo>? = null,

        @SerializedName("tblAssetMaintenanceInfos")
        val tblAssetMaintenanceInfos: List<AssetMaintenanceInfo>? = null,

        @SerializedName("tblAssetBOMInfos")
        val tblAssetBOMInfos: List<BillOfMaterial>? = null
    )
}