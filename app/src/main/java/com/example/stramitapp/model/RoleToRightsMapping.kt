package com.example.stramitapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_role_to_rights_mapping")
class RoleToRightsMapping (
    @PrimaryKey(autoGenerate = true)
    var mappingId: Int = 0,
    var roleId: Int = 0,
    var rightsId: Int = 0
): BaseDataObject()