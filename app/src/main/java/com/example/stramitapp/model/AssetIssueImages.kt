package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.models.Interfaces.Base.IBaseLocalModel

@Entity(tableName = "tbl_asset_issue_images")
class AssetIssueImages : IBaseLocalModel {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "asset_issue_id")
    var assetIssueId: Int = 0

    @ColumnInfo(name = "type")
    var type: String? = null

    @ColumnInfo(name = "image_type")
    var imageType: String? = null

    @ColumnInfo(name = "image_path")
    var imagePath: String? = null

    @ColumnInfo(name = "updated_by")
    var updatedBy: Int = 0

    @ColumnInfo(name = "update_flag")
    var updateFlag: String? = null

    @ColumnInfo(name = "last_update_date")
    var lastUpdateDate: String? = null

    @ColumnInfo(name = "flag_sync")
    var flagSync: Int = 0

    @ColumnInfo(name = "DTS_mm_sync")
    var dTSMMSync: String? = null

    @ColumnInfo(name = "STD_mm_sync")
    var sTDMMSync: String? = null

    @ColumnInfo(name = "image_sync")
    var imageSync: String? = null
}