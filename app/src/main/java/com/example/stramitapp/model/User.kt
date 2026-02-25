package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.stramitapp.Licensee
import com.example.stramitapp.model.CompanyAssetCategory
import com.example.stramitapp.model.CompanyLocation
import com.example.stramitapp.model.DataObject.BaseDataObject
import com.example.stramitapp.model.UserRole

@Entity(tableName = "tbl_user")
class User : BaseDataObject() {

    @PrimaryKey
    @ColumnInfo(name = "user_id")
    var userId: Int = 0

    @ColumnInfo(name = "first_name")
    var firstName: String? = null

    @ColumnInfo(name = "middle_name")
    var middleName: String? = null

    @ColumnInfo(name = "last_name")
    var lastName: String? = null

    @ColumnInfo(name = "email")
    var email: String? = null

    @ColumnInfo(name = "phone")
    var phone: String? = null

    @ColumnInfo(name = "loginname")
    var loginName: String? = null

    @ColumnInfo(name = "password")
    var password: String? = null

    @ColumnInfo(name = "parent_user_id")
    var parentUserId: Int = 0

    @ColumnInfo(name = "current_device_udid")
    var currentDeviceUdid: String? = null

    @ColumnInfo(name = "current_device_type")
    var currentDeviceType: Int? = null

    @ColumnInfo(name = "licensee_id")
    var licenseeId: Int? = null

    @ColumnInfo(name = "is_active")
    var isActive: Int = 0

    @Ignore
    var tblLicensees: List<Licensee>? = null

    @Ignore
    var tblUserRoles: List<UserRole>? = null

    @Ignore
    var tblCompanyAssetCategories: List<CompanyAssetCategory>? = null

    @Ignore
    var tblCompanyLocations: List<CompanyLocation>? = null

    @Ignore
    var passwordGeneratorNumber: String? = null

    @Ignore
    var isWebUser: Int = 0

    @Ignore
    var isDeviceUser: Int = 0

    @Ignore
    var isInspectionUser: Int = 0

    @Ignore
    var isMaintenanceUser: Int = 0
}