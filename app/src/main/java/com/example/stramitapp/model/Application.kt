package com.example.stramitapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_application")
data class Application(
    @PrimaryKey
    var appId: Int = 0,
    var appName: String = ""
) : BaseDataObject()