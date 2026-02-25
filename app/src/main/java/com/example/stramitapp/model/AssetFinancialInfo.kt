package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.models.Interfaces.Base.IBaseLocalModel

@Entity(tableName = "tbl_asset_financial_info")
class AssetFinancialInfo : IBaseLocalModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "asset_id")
    var assetId: Int = 0

    @ColumnInfo(name = "device_id")
    var deviceId: Int = 0

    @ColumnInfo(name = "finance_asset_id")
    var financeAssetId: String? = null

    @ColumnInfo(name = "attribute_device_id")
    var attributeDeviceId: Int = 0

    @ColumnInfo(name = "cost")
    var cost: Float = 0f

    @ColumnInfo(name = "cost_center_id")
    var costCenterId: Int? = null

    @ColumnInfo(name = "currency_indicator")
    var currencyIndicator: Int? = null

    @ColumnInfo(name = "owner_id")
    var ownerId: String? = null

    @ColumnInfo(name = "supplier_id")
    var supplierId: Int? = null

    @ColumnInfo(name = "depreciation_date")
    var depreciationDate: String? = null

    @ColumnInfo(name = "depreciated_value")
    var depreciatedValue: Float? = null

    @ColumnInfo(name = "write_of_date")
    var writeOfDate: String? = null

    @ColumnInfo(name = "write_of_reason")
    var writeOfReason: String? = null

    @ColumnInfo(name = "dispose_condition_id")
    var disposeConditionId: Int = 0

    @ColumnInfo(name = "dispose_person_name")
    var disposePersonName: String? = null

    @ColumnInfo(name = "updated_by")
    var updateBy: Int = 0

    @ColumnInfo(name = "update_flag")
    var updateFlag: String? = null

    @ColumnInfo(name = "dispose_flag")
    var disposeFlag: String? = null

    @ColumnInfo(name = "last_update_date")
    var lastUpdateDate: String? = null

    @ColumnInfo(name = "flag_sync")
    var flagSync: Int = 0
}