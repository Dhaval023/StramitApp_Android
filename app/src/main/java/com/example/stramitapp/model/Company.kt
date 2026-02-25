package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.models.Interfaces.Base.IBaseLocalModel
import java.time.OffsetDateTime

@Entity(tableName = "tbl_company")
class Company : IBaseLocalModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "company_id")
    var companyId: Int = 0

    @ColumnInfo(name = "company_name")
    var companyName: String? = null

    @ColumnInfo(name = "licensee_id")
    var licenseeId: Int = 0

    @ColumnInfo(name = "admin_id")
    var adminId: Int? = null

    @ColumnInfo(name = "update_flag")
    var updateFlag: String? = null

    @ColumnInfo(name = "last_update_date")
    var lastUpdateDate: OffsetDateTime? = null

    @ColumnInfo(name = "flag_sync")
    var flagSync: Int = 0

    @ColumnInfo(name = "multi_scan")
    var multiScan: String? = null
}