package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.models.Interfaces.Base.IBaseLocalModel

@Entity(tableName = "tbl_asset")
class Asset : IBaseLocalModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "asset_id")
    var assetId: Int = 0

    @ColumnInfo(name = "device_id")
    var deviceId: Int = 0

    @ColumnInfo(name = "last_update_device_id")
    var lastUpdateDeviceId: Int = 0

    @ColumnInfo(name = "company_asset_id")
    var companyAssetId: String? = null

    @ColumnInfo(name = "asset_type")
    var assetType: Int? = null

    @ColumnInfo(name = "title")
    var title: String? = null

    @ColumnInfo(name = "tag")
    var tag: String? = null

    @ColumnInfo(name = "barcode")
    var barcode: String? = null

    @ColumnInfo(name = "serial_number")
    var serialNumber: String? = null

    @ColumnInfo(name = "value")
    var assetValue: Float? = null

    @ColumnInfo(name = "part_id")
    var partId: Int? = null

    @ColumnInfo(name = "location_id")
    var locationId: Int? = null

    @ColumnInfo(name = "category_id")
    var categoryId: Int? = null

    @ColumnInfo(name = "status_id")
    var statusId: Int? = null

    @ColumnInfo(name = "company_id")
    var companyId: Int? = null

    @ColumnInfo(name = "condition_id")
    var conditionId: Int? = null

    @ColumnInfo(name = "long_desc")
    var longDesc: String? = null

    @ColumnInfo(name = "gps_lat")
    var gpsLat: Float? = null

    @ColumnInfo(name = "gps_long")
    var gpsLong: Float? = null

    @ColumnInfo(name = "asset_image")
    var assetImage: String? = null

    @ColumnInfo(name = "create_date")
    var createDate: String? = null

    @ColumnInfo(name = "purchase_date")
    var purchaseDate: String? = null

    @ColumnInfo(name = "last_update_date")
    var lastUpdateDate: String? = null

    @ColumnInfo(name = "created_by")
    var createdBy: Int? = null

    @ColumnInfo(name = "responsible_user_id")
    var responsibleUserId: Int? = null

    @ColumnInfo(name = "last_updated_by")
    var lastUpdatedBy: Int? = null

    @ColumnInfo(name = "update_flag")
    var updateFlag: String? = null

    @ColumnInfo(name = "DTS_mm_sync")
    var dTSMMSync: String? = null

    @ColumnInfo(name = "STD_mm_sync")
    var sTDMMSync: String? = null

    @ColumnInfo(name = "flag_sync")
    var flagSync: Int? = null

    @ColumnInfo(name = "image_sync")
    var imageSync: Int = 0

    @ColumnInfo(name = "weight")
    var weight: Float? = null

    @ColumnInfo(name = "weight_uom")
    var weightUom: Int? = null

    // 36 Custom fields
    @ColumnInfo(name = "custom_1")
    var custom1: String? = null // length

    @ColumnInfo(name = "custom_2")
    var custom2: String? = null // width

    @ColumnInfo(name = "custom_3")
    var custom3: String? = null // girth

    @ColumnInfo(name = "custom_4")
    var custom4: String? = null // color

    @ColumnInfo(name = "custom_5")
    var custom5: String? = null // numberOfBends

    @ColumnInfo(name = "custom_6")
    var custom6: String? = null // packNumber

    @ColumnInfo(name = "custom_7")
    var custom7: String? = null // totalPackNumber

    @ColumnInfo(name = "custom_8")
    var custom8: String? = null // splitNumber

    @ColumnInfo(name = "custom_9")
    var custom9: String? = null // supplier reference

    @ColumnInfo(name = "custom_10")
    var custom10: String? = null // supplier number

    @ColumnInfo(name = "custom_11")
    var custom11: String? = null // docketNumber

    @ColumnInfo(name = "custom_12")
    var custom12: String? = null // purchaseOrderNumber

    @ColumnInfo(name = "custom_13")
    var custom13: String? = null // erpOrderNumber

    @ColumnInfo(name = "custom_14")
    var custom14: String? = null // customer name

    @ColumnInfo(name = "custom_15")
    var custom15: String? = null // customerReference

    @ColumnInfo(name = "custom_16")
    var custom16: String? = null // deliveryNumber

    @ColumnInfo(name = "custom_17")
    var custom17: String? = null // dropNumber

    @ColumnInfo(name = "custom_18")
    var custom18: String? = null // shipmentNumber

    @ColumnInfo(name = "custom_19")
    var custom19: String? = null // route

    @ColumnInfo(name = "custom_20")
    var custom20: String? = null // deliveryInstruction

    @ColumnInfo(name = "custom_21")
    var custom21: String? = null // address

    @ColumnInfo(name = "custom_22")
    var custom22: String? = null // distributionOrderNumber

    @ColumnInfo(name = "custom_23")
    var custom23: String? = null // quantity

    @ColumnInfo(name = "custom_24")
    var custom24: String? = null // quantityUOM

    @ColumnInfo(name = "custom_25")
    var custom25: String? = null // lengthUOM

    @ColumnInfo(name = "custom_26")
    var custom26: String? = null // heightUOM

    @ColumnInfo(name = "custom_27")
    var custom27: String? = null // widthUOM

    @ColumnInfo(name = "custom_28")
    var custom28: String? = null // weightUOM

    @ColumnInfo(name = "custom_29")
    var custom29: String? = null // girthUOM

    @ColumnInfo(name = "custom_30")
    var custom30: String? = null // colourUOM

    @ColumnInfo(name = "custom_31")
    var custom31: String? = null // packageStructure

    @ColumnInfo(name = "custom_32")
    var custom32: String? = null // manufacturingInstruction

    @ColumnInfo(name = "custom_33")
    var custom33: String? = null // scheduleNumber

    @ColumnInfo(name = "custom_34")
    var custom34: String? = null // markNumber

    @ColumnInfo(name = "custom_35")
    var custom35: String? = null // packDescription

    @ColumnInfo(name = "custom_36")
    var custom36: String? = null // PackageNumber
}