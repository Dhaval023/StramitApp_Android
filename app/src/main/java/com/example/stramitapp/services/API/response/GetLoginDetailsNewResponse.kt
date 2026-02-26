package com.example.stramitapp.services.API.response

import com.example.stramitapp.Licensee
import com.example.stramitapp.model.*

data class GetLoginDetailsNewResponse(
    var statusCode: Int = 0,
    var error: String? = null,
    var success: String? = null,
    var databaseTimeStamp: String? = null,
    var list: List<ListItem>? = null
) {
    data class ListItem(
        var userId: Int = 0,
        var firstName: String? = null,
        var middleName: String? = null,
        var lastName: String? = null,
        var email: String? = null,
        var phone: String? = null,
        var loginname: String? = null,
        var password: String? = null,
        var parentUserId: Int = 0,
        var currentDeviceUdid: String? = null,
        var currentDeviceType: Int? = null,
        var licenseeId: Int? = null,
        var isActive: Int = 0,
        var tblLicensees: List<Licensee>? = null,
        var tblUserRoles: List<UserRole>? = null,
        var tblCompanyAssetCategories: List<CompanyAssetCategory>? = null,
        var tblCompanyLocations: List<CompanyLocation>? = null,
        var passwordGeneratorNumber: String? = null,
        var isWebUser: Int = 0,
        var isDeviceUser: Int = 0,
        var isInspectionUser: Int = 0,
        var isMaintenanceUser: Int = 0
    )
}