package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_company_asset_parts")
class CompanyAssetParts : BaseDataObject() {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "part_id")
    var partId: Int = 0

    @ColumnInfo(name = "part_name")
    var partName: String? = null

    @ColumnInfo(name = "company_id")
    var companyId: Int = 0

    @ColumnInfo(name = "updated_by")
    var updateBy: Int = 0

    @ColumnInfo(name = "update_flag")
    var updateFlag: String? = null

    @ColumnInfo(name = "last_update_date")
    var lastUpdateDate: String? = null

    @ColumnInfo(name = "flag_sync")
    var flagSync: Int = 0

    @ColumnInfo(name = "parent_part_id")
    var parentPartId: Int = 0
}