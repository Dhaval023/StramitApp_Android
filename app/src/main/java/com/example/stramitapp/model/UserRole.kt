package com.example.stramitapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_user_role")
data class UserRole(
    @PrimaryKey
    @ColumnInfo(name = "role_id")
    val roleId: Int,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "company_id")
    val companyId: Int,

    @ColumnInfo(name = "application_id")
    val applicationId: Int? = null
) : BaseDataObject()