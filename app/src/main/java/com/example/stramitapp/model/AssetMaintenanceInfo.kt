package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.models.Interfaces.Base.IBaseLocalModel

@Entity(tableName = "tbl_asset_maintenance_info")
class AssetMaintenanceInfo : IBaseLocalModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "asset_id")
    var assetId: Int = 0

    @ColumnInfo(name = "device_id")
    var deviceId: Int = 0

    @ColumnInfo(name = "attribute_device_id")
    var attributeDeviceId: Int = 0

    @ColumnInfo(name = "maintenance_date")
    var maintenanceDate: String? = null

    @ColumnInfo(name = "maintained_by")
    var maintainedBy: Int? = null

    @ColumnInfo(name = "is_external_maintenance")
    var isExternalMaintenance: Boolean? = null

    @ColumnInfo(name = "work_order_number")
    var workOrderNumber: Int? = null

    @ColumnInfo(name = "description")
    var description: String? = null

    @ColumnInfo(name = "updated_by")
    var updatedBy: Int = 0

    @ColumnInfo(name = "update_flag")
    var updateFlag: String? = null

    @ColumnInfo(name = "last_update_date")
    var lastUpdateDate: String? = null

    @ColumnInfo(name = "flag_sync")
    var flagSync: Int = 0
}