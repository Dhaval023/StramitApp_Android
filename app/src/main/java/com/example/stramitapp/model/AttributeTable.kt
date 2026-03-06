package com.example.stramitapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_attribute_table")
class AttributeTable : BaseDataObject() {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    override var id: Int = 0

    @ColumnInfo(name = "table_name")
    var tableName: String? = null

    @ColumnInfo(name = "display_name")
    var displayName: String? = null
}