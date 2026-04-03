package com.example.stramitapp.common.API.Login.response

import com.google.gson.annotations.SerializedName

data class GetLoginDetailsNewResponse(

    @SerializedName("statusCode")
    var statusCode: Long = 0,

    @SerializedName("error")
    var error: String? = null,

    @SerializedName("success")
    var success: String? = null,

    @SerializedName("databaseTimeStamp")
    var databaseTimeStamp: Any? = null,

    @SerializedName("list")
    var list: List<ListItem>? = null
) {

    data class ListItem(

        @SerializedName("userId")
        var userId: Long = 0,

        @SerializedName("firstName")
        var firstName: String? = null,

        @SerializedName("middleName")
        var middleName: String? = null,

        @SerializedName("lastName")
        var lastName: String? = null,

        @SerializedName("email")
        var email: String? = null,

        @SerializedName("phone")
        var phone: String? = null,

        @SerializedName("loginname")
        var loginname: String? = null,

        @SerializedName("password")
        var password: String? = null,

        @SerializedName("parentUserId")
        var parentUserId: Long = 0,

        @SerializedName("currentDeviceUdid")
        var currentDeviceUdid: String? = null,

        @SerializedName("currentDeviceType")
        var currentDeviceType: Long = 0,

        @SerializedName("licenseeId")
        var licenseeId: Long = 0,

        @SerializedName("isActive")
        var isActive: Long = 0,

        @SerializedName("tblLicensees")
        var tblLicensees: List<Any>? = null,

        @SerializedName("tblUserRoles")
        var tblUserRoles: List<Any>? = null,

        @SerializedName("tblCompanyAssetCategories")
        var tblCompanyAssetCategories: List<Any>? = null,

        @SerializedName("tblCompanyLocations")
        var tblCompanyLocations: List<Any>? = null,

        @SerializedName("passwordGeneratorNumber")
        var passwordGeneratorNumber: String? = null,

        @SerializedName("isWebUser")
        var isWebUser: Long = 0,

        @SerializedName("isDeviceUser")
        var isDeviceUser: Long = 0,

        @SerializedName("isInspectionUser")
        var isInspectionUser: Long = 0,

        @SerializedName("isMaintenanceUser")
        var isMaintenanceUser: Long = 0
    )

}