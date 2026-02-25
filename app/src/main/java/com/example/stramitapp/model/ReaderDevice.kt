package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_reader")
class ReaderDevice : BaseDataObject() {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var name: String = ""
    var address: String = ""
    var isPaired: Boolean = false
    var isConnected: Boolean = false
    var readerModelId: Int = 0
}