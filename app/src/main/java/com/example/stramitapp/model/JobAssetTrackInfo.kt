package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(
    tableName = "tbl_job_asset_track_info",
    indices = [Index(value = ["job_id", "asset_id"], unique = true)]
)
class JobAssetTrackInfo : BaseDataObject() {

    @ColumnInfo(name = "job_id")
    var jobId: Int = 0

    @ColumnInfo(name = "asset_id")
    var assetId: Int = 0

    @ColumnInfo(name = "device_id")
    var deviceId: Int = 0

    @ColumnInfo(name = "asset_condition")
    var assetConditionId: Int? = null

    @ColumnInfo(name = "track_date")
    var trackDate: String? = null

    @ColumnInfo(name = "track_by")
    var trackBy: Int? = null

    @ColumnInfo(name = "asset_image")
    var assetImage: String? = null

    @ColumnInfo(name = "location_id")
    var locationId: Int? = null

    @ColumnInfo(name = "assetTrackStatus")
    var assetTrackStatus: Int = 0

    @ColumnInfo(name = "update_flag")
    var updateFlag: String? = null

    @ColumnInfo(name = "last_update_date")
    var lastUpdateDate: String? = null

    @ColumnInfo(name = "flag_status")
    var flagStatus: Int = 0

    @ColumnInfo(name = "job_performed_loc_id")
    var jobPerformedLocId: Int? = null
}