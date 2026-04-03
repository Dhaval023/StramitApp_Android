package com.example.stramitapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject
@Entity(tableName = "tbl_role")
class Role  (
    @PrimaryKey
    var roleId: Int = 0,
    var roleName: String = "",
    var createdBy: Int = 0,
    var description: String = "",
    var isActive: Int = 0,
    var roleType: Int = 0
): BaseDataObject()