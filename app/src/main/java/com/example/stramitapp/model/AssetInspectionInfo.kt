package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_asset_inspection_info")
class AssetInspectionInfo : BaseDataObject() {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "asset_id")
    var assetId: Int = 0

    @ColumnInfo(name = "device_id")
    var deviceId: Int = 0

    @ColumnInfo(name = "attribute_device_id")
    var attributeDeviceId: Int = 0

    @ColumnInfo(name = "date_of_inspection")
    var dateOfInspection: String? = null

    @ColumnInfo(name = "inspected_by")
    var inspectedBy: Int? = null

    @ColumnInfo(name = "is_external_inspection")
    var isExternalInspection: Boolean? = null

    @ColumnInfo(name = "certificate_number")
    var certificateNumber: String? = null

    @ColumnInfo(name = "expiry_date")
    var expiryDate: String? = null

    @ColumnInfo(name = "updated_by")
    var updatedBy: Int = 0

    @ColumnInfo(name = "update_flag")
    var updateFlag: String? = null

    @ColumnInfo(name = "last_update_date")
    var lastUpdateDate: String? = null

    @ColumnInfo(name = "flag_sync")
    var flagSync: Int = 0
}