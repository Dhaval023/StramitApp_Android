package com.example.stramitapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_attribute")
class Attributes : BaseDataObject() {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    override var id: Int = 0

    @ColumnInfo(name = "table_id")
    var tableId: Int = 0

    @ColumnInfo(name = "column_name")
    var columnName: String? = null

    @ColumnInfo(name = "is_mandatory")
    var isMandatory: Boolean = false

    @ColumnInfo(name = "column_display_name")
    var columnDisplayName: String? = null

    @ColumnInfo(name = "is_readonly")
    var isReadonly: Boolean = false
}