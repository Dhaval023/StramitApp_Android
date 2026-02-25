package com.example.stramitapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tbl_company_location")
data class CompanyLocation(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "location_id")
    var locationId: Int = 0,

    @ColumnInfo(name = "location_name")
    var locationName: String? = null,

    @ColumnInfo(name = "location_barcode")
    var locationBarcode: String? = null,

    @ColumnInfo(name = "location_tag")
    var tag: String? = null,

    @ColumnInfo(name = "is_root_location")
    var isRootLocation: Boolean = false,

    @ColumnInfo(name = "parent_location_id")
    var parentLocationId: Int = 0,

    @ColumnInfo(name = "company_id")
    var companyId: Int = 0,

    @ColumnInfo(name = "update_flag")
    var updateFlag: String = "I",

    @ColumnInfo(name = "last_update_date")
    var lastUpdateDate: Date? = null,

    @ColumnInfo(name = "responsible_user_id")
    var responsibleUserId: Int? = null,

    @ColumnInfo(name = "flag_sync")
    var flagSync: Int = 0
)