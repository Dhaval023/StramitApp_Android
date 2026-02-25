package com.example.stramitapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_company_asset_category")
data class CompanyAssetCategory(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "parent_category_id")
    val parentCatId: Int = 0,

    @ColumnInfo(name = "category_name")
    val categoryName: String,

    @ColumnInfo(name = "responsible_user_id")
    val responsibleUserId: Int? = null,

    @ColumnInfo(name = "company_id")
    val companyId: Int,

    @ColumnInfo(name = "updated_by")
    val updatedBy: Int? = null,

    @ColumnInfo(name = "update_flag")
    val updateFlag: String = "I",

    @ColumnInfo(name = "last_update_date")
    val lastUpdateDate: String? = null,

    @ColumnInfo(name = "flag_sync")
    val flagSync: Int = 0
) : BaseDataObject()