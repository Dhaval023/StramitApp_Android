package com.example.stramitapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.models.Interfaces.Base.IBaseLocalModel

@Entity(tableName = "tbl_device_type")
class DeviceType (
    @PrimaryKey
    override var id: Int = 0,
    var name: String = ""
): IBaseLocalModel