package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject
import java.time.OffsetDateTime

@Entity(tableName = "tbl_job_description")
class JobDescription : BaseDataObject() {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "job_desc_id")
    var jobDescId: Int = 0

    @ColumnInfo(name = "job_desc")
    var jobDesc: String? = null

    @ColumnInfo(name = "is_active")
    var isActive: Int = 0

    @ColumnInfo(name = "created_by")
    var createdBy: Int = 0

    @ColumnInfo(name = "last_updated_user")
    var lastUpdatedUser: Int = 0

    @ColumnInfo(name = "create_date")
    var createDate: OffsetDateTime? = null

    @ColumnInfo(name = "last_update_date")
    var lastUpdateDate: OffsetDateTime? = null
}