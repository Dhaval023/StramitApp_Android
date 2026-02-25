package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_asset_lease_info")
class AssetLeaseInfo : BaseDataObject() {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "asset_id")
    var assetId: Int = 0

    @ColumnInfo(name = "device_id")
    var deviceId: Int = 0

    @ColumnInfo(name = "attribute_device_id")
    var attributeDeviceId: Int = 0

    @ColumnInfo(name = "leasing_company_name")
    var leasingCompanyName: String? = null

    @ColumnInfo(name = "lease_start_date")
    var leaseStartDate: String? = null

    @ColumnInfo(name = "lease_end_date")
    var leaseEndDate: String? = null

    @ColumnInfo(name = "contract_number")
    var contractNumber: String? = null

    @ColumnInfo(name = "is_service_covered_under_lease")
    var isServiceCoveredUnderLease: Boolean? = null

    @ColumnInfo(name = "updated_by")
    var updatedBy: Int = 0

    @ColumnInfo(name = "update_flag")
    var updateFlag: String? = null

    @ColumnInfo(name = "last_update_date")
    var lastUpdateDate: String? = null

    @ColumnInfo(name = "flag_sync")
    var flagSync: Int = 0
}