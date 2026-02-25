package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_asset_issue_info")
class AssetIssueInfo : BaseDataObject() {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "asset_id")
    var assetId: Int = 0

    @ColumnInfo(name = "device_id")
    var deviceId: Int = 0

    @ColumnInfo(name = "attribute_device_id")
    var attributeDeviceId: Int = 0

    @ColumnInfo(name = "type")
    var type: String? = null

    @ColumnInfo(name = "issue_to")
    var issueTo: Int? = null

    @ColumnInfo(name = "issue_to_supplierId")
    var issueToSupplierId: Int = 0

    @ColumnInfo(name = "date")
    var date: String? = null

    @ColumnInfo(name = "description")
    var description: String? = null

    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "condition_id")
    var conditionId: Int = 0

    @ColumnInfo(name = "updated_by")
    var updatedBy: Int = 0

    @ColumnInfo(name = "update_flag")
    var updateFlag: String? = null

    @ColumnInfo(name = "last_update_date")
    var lastUpdateDate: String? = null

    @ColumnInfo(name = "flag_sync")
    var flagSync: Int? = null

    @ColumnInfo(name = "work_order_number")
    var workOrderNumber: String? = null

    @ColumnInfo(name = "company_id")
    var companyId: Int? = null
}