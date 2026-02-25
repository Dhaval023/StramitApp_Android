package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_company_asset_sub_parts")
class CompanyAssetSubParts : BaseDataObject() {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sub_part_id")
    var subPartId: Int = 0

    @ColumnInfo(name = "sub_part_desc")
    var subPartDesc: String = ""

    @ColumnInfo(name = "asset_type_id")
    var assetTypeId: Int = 0

    @ColumnInfo(name = "part_id")
    var partId: Int = 0

    @ColumnInfo(name = "parent_part_id")
    var parentPartId: Int = 0

    @ColumnInfo(name = "update_flag")
    var updateFlag: String = ""

    @ColumnInfo(name = "last_update_date")
    var lastUpdateDate: String = ""

    @ColumnInfo(name = "company_id")
    var companyId: String? = null

    @ColumnInfo(name = "update_by")
    var updateBy: String? = null

    @ColumnInfo(name = "flag_sync")
    var flagSync: String? = null
}