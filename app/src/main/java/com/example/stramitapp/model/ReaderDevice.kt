package com.example.stramitapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_reader")
class ReaderDevice : BaseDataObject() {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    override var id: Int = 0

    @ColumnInfo(name = "name")
    var name: String = ""

    @ColumnInfo(name = "address")
    var address: String = ""

    @ColumnInfo(name = "is_paired")
    var isPaired: Boolean = false

    @ColumnInfo(name = "is_connected")
    var isConnected: Boolean = false

    @ColumnInfo(name = "reader_model_id")
    var readerModelId: Int = 0
}