package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_job_definition")
class JobDefinition : BaseDataObject() {

    @PrimaryKey
    @ColumnInfo(name = "job_id")
    var jobId: Int = 0

    @ColumnInfo(name = "job_name")
    var jobName: String? = null

    @ColumnInfo(name = "company_id")
    var companyId: Int = 0

    @ColumnInfo(name = "location_id")
    var locationId: Int? = null

    @ColumnInfo(name = "job_desc")
    var jobDesc: String? = null

    @ColumnInfo(name = "creation_date")
    var creationDate: String? = null

    @ColumnInfo(name = "created_by")
    var createdBy: Int? = null

    @ColumnInfo(name = "submittion_date")
    var submittionDate: String? = null

    @ColumnInfo(name = "update_flag")
    var updateFlag: String? = null

    @ColumnInfo(name = "last_update_date")
    var lastUpdateDate: String? = null
}

class MixJobDefinitionAssigment {
    var jobId: Int = 0
    var jobName: String? = null
    var companyId: Int = 0
    var locationId: Int? = null
    var jobDesc: String? = null
    var creationDate: String? = null
    var createdBy: Int? = null
    var submittionDate: String? = null
    var updateFlag: String? = null
    var lastUpdateDate: String? = null
    var assigStatus: Int? = null
}