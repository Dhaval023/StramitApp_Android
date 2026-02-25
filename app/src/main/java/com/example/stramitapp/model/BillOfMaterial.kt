package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_bill_of_material")
class BillOfMaterial : BaseDataObject() {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "asset_id")
    var assetId: Int? = null

    @ColumnInfo(name = "device_id")
    var deviceId: Int? = null

    @ColumnInfo(name = "company_id")
    var companyId: Int = 0

    @ColumnInfo(name = "sub_part_id")
    var subPartId: Int? = null

    @ColumnInfo(name = "sub_part_asset_id")
    var subPartAssetId: Int? = null

    @ColumnInfo(name = "sub_part_device_id")
    var subPartDeviceId: Int? = null

    @ColumnInfo(name = "update_flag")
    var updateFlag: String? = null

    @ColumnInfo(name = "update_by")
    var updateBy: Int = 0

    @ColumnInfo(name = "last_update_date")
    var lastUpdateDate: String? = null

    @ColumnInfo(name = "flag_sync")
    var flagSync: Int = 0
}