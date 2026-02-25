package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_licensee_config")
class LicenseeConfig : BaseDataObject() {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = ""

    @ColumnInfo(name = "item")
    var item: String? = null

    @ColumnInfo(name = "value")
    var value: String? = null
}