package com.example.stramitapp.services.API.response

//import com.example.stramitapp.Licensee
import com.google.gson.annotations.SerializedName
import com.example.stramitapp.model.*

data class GetLoginDetailsNewResponse(
    @SerializedName("statusCode")
    var statusCode: Int = 0,

    @SerializedName("error")
    var error: String? = null,

    @SerializedName("success")
    var success: String? = null,

    @SerializedName("databaseTimeStamp")
    var databaseTimeStamp: String? = null,

    @SerializedName("list")
    var list: List<ListItem>? = null
) {
    data class ListItem(
        @SerializedName("userId") var userId: Int = 0,
        @SerializedName("firstName") var firstName: String? = null,
        @SerializedName("middleName") var middleName: String? = null,
        @SerializedName("lastName") var lastName: String? = null,
        @SerializedName("email") var email: String? = null,
        @SerializedName("phone") var phone: String? = null,
        @SerializedName("loginname") var loginname: String? = null,
        @SerializedName("password") var password: String? = null,

        // ADD @SerializedName to these — they were missing
        @SerializedName("parentUserId") var parentUserId: Int = 0,
        @SerializedName("currentDeviceUdid") var currentDeviceUdid: String? = null,
        @SerializedName("currentDeviceType") var currentDeviceType: Int? = null,
        @SerializedName("licenseeId") var licenseeId: Int? = null,
        @SerializedName("isActive") var isActive: Int = 0,

        // CHANGE List<Licensee/UserRole/etc> to List<Any> — Room @Entity classes break Gson
        @SerializedName("tblLicensees") var tblLicensees: List<Any>? = null,
        @SerializedName("tblUserRoles") var tblUserRoles: List<Any>? = null,
        @SerializedName("tblCompanyAssetCategories") var tblCompanyAssetCategories: List<Any>? = null,
        @SerializedName("tblCompanyLocations") var tblCompanyLocations: List<Any>? = null,

        @SerializedName("passwordGeneratorNumber") var passwordGeneratorNumber: String? = null,
        @SerializedName("isWebUser") var isWebUser: Int = 0,
        @SerializedName("isDeviceUser") var isDeviceUser: Int = 0,
        @SerializedName("isInspectionUser") var isInspectionUser: Int = 0,
        @SerializedName("isMaintenanceUser") var isMaintenanceUser: Int = 0
    )
}