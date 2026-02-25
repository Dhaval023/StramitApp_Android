package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_setting")
class Settings : BaseDataObject() {

    @PrimaryKey
    @ColumnInfo(name = "key")
    var key: String = ""

    @ColumnInfo(name = "value")
    var value: String? = null
}