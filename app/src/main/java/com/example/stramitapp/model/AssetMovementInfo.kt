package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.models.Interfaces.Local.BaseLocalModel

@Entity(tableName = "tbl_asset_movement_info")
class AssetMovementInfo : BaseLocalModel() {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "asset_id")
    var assetId: Int = 0

    @ColumnInfo(name = "device_id")
    var deviceId: Int = 0

    @ColumnInfo(name = "attribute_device_id")
    var attributeDeviceId: Int = 0

    @ColumnInfo(name = "source_location_id")
    var sourceLocationId: Int = 0

    @ColumnInfo(name = "destination_location_id")
    var destinationLocationId: Int = 0

    @ColumnInfo(name = "movement_date")
    var movementDate: String? = null

    @ColumnInfo(name = "moved_by")
    var movedBy: Int? = null

    @ColumnInfo(name = "movement_type")
    var movementType: String? = null

    @ColumnInfo(name = "movement_recorded_by")
    var movementRecordedBy: Int? = null

    @ColumnInfo(name = "updated_by")
    var updatedBy: Int = 0

    @ColumnInfo(name = "update_flag")
    var updateFlag: String? = null

    @ColumnInfo(name = "last_update_date")
    var lastUpdateDate: String? = null

    @ColumnInfo(name = "flag_sync")
    var flagSync: Int = 0

    @ColumnInfo(name = "work_order_number")
    var workOrderNumber: String? = null
}