package com.example.stramitapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_rights")
class Rights (
    @PrimaryKey
    var rightsId: Int = 0,
    var rightsName: String = "",
    var description: String = "",
    var rightFlag: Boolean = false
): BaseDataObject()