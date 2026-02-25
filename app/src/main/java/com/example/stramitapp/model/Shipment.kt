package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.models.Interfaces.Base.IBaseLocalModel

@Entity(tableName = "tbl_shipment")
class Shipment : IBaseLocalModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "shipment_id")
    var shipmentId: Int? = null

    @ColumnInfo(name = "shipment_number")
    var shipmentNumber: String? = null

    @ColumnInfo(name = "shipment_status")
    var shipmentStatus: String? = null

    @ColumnInfo(name = "last_update_date")
    var lastUpdateDate: String? = null

    @ColumnInfo(name = "update_flag")
    var updateFlag: String? = null

    @ColumnInfo(name = "updated_by")
    var updatedBy: Int? = null
}