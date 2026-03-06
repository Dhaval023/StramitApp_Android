package com.example.stramitapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_reader")
class ReaderModel1 : BaseDataObject() {

    @PrimaryKey(autoGenerate = true)
   override var id: Int = 0
    var name: String = ""
    var imageSource: String = ""
    var series: String = ""
    var isDefault: Boolean = false
}