package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject
import java.time.OffsetDateTime

@Entity(tableName = "tbl_mobile_job")
class MobileJob : BaseDataObject() {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "job_id")
    var jobId: Int = 0

    @ColumnInfo(name = "company_wp_id")
    var companyWpId: Int = 0

    @ColumnInfo(name = "company_id")
    var companyId: Int = 0

    @ColumnInfo(name = "location_id")
    var locationId: Int = 0

    @ColumnInfo(name = "job_type_id")
    var jobTypeId: Int = 0

    @ColumnInfo(name = "job_desc_id")
    var jobDescId: Int = 0

    @ColumnInfo(name = "asset_id")
    var assetId: String? = null

    @ColumnInfo(name = "user_id")
    var userId: Int = 0

    @ColumnInfo(name = "is_active")
    var isActive: Int = 0

    @ColumnInfo(name = "job_number")
    var jobNumber: String? = null

    @ColumnInfo(name = "last_updated_user")
    var lastUpdatedUser: Int = 0

    @ColumnInfo(name = "create_date")
    var createDate: OffsetDateTime? = null

    @ColumnInfo(name = "last_update_date")
    var lastUpdateDate: OffsetDateTime? = null
}