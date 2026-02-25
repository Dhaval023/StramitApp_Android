package com.example.stramitapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_reader")
class ReaderModel1 : BaseDataObject() {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var name: String = ""
    var imageSource: String = ""
    var series: String = ""
    var isDefault: Boolean = false
}