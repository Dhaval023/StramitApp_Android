package com.example.stramitapp.services.API.Sync.response

import com.example.stramitapp.models.AssetMemoType
import com.example.stramitapp.models.BillOfMaterial
import com.example.stramitapp.models.CompanyAssetType
import com.google.gson.annotations.SerializedName

data class GetAssignCompanyListToUserResponse(

    var statusCode: Int = 0,
    var error: String? = null,
    var success: String? = null,
    var databaseTimeStamp: String? = null,
    var list: List<CompanyList>? = null
) {
    data class CompanyList(

        @SerializedName("companyId")
        val companyId: Int = 0,

        @SerializedName("companyName")
        val companyName: String? = null,

        @SerializedName("licenseeId")
        val licenseeId: Int = 0,

        @SerializedName("adminId")
        val adminId: Int = 0,

        @SerializedName("companyAdminName")
        val companyAdminName: String? = null,

        @SerializedName("isActive")
        val isActive: Int = 0,

        @SerializedName("multiScan")
        val multiScan: String? = null,

        @SerializedName("tblAssetMemoType")
        val tblAssetMemoType: List<AssetMemoType>? = null,

        @SerializedName("tblCompanyAssetTypes")
        val tblCompanyAssetTypes: List<CompanyAssetType>? = null,

        @SerializedName("tblAssetBOMInfos")
        val tblAssetBOMInfos: List<BillOfMaterial>? = null,

        @SerializedName("updateFlag")
        val updateFlag: String? = null,

        @SerializedName("isManageVisible")
        val isManageVisible: Boolean = false,

        @SerializedName("deliveryAddress")
        val deliveryAddress: String? = null,

        @SerializedName("postalAddress")
        val postalAddress: String? = null,

        @SerializedName("importAsset")
        val importAsset: Boolean = false
    )
}