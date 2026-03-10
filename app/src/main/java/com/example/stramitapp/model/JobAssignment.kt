package com.example.stramitapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(
    tableName = "tbl_job_assignment",
    primaryKeys = ["job_id", "assign_to"])
class JobAssignment : BaseDataObject() {

    @PrimaryKey
    @ColumnInfo(name = "job_id")
    var jobId: Int = 0

    @ColumnInfo(name = "assign_to")
    var assignTo: Int = 0

    @ColumnInfo(name = "device_udid")
    var deviceUdid: String? = null

    @ColumnInfo(name = "status")
    var status: Int? = null

    @ColumnInfo(name = "progress_status")
    var progressStatus: Int = 0

    @ColumnInfo(name = "update_flag")
    var updateFlag: String? = null

    @ColumnInfo(name = "last_update_date")
    var lastUpdateDate: String? = null
}