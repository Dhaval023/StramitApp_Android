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