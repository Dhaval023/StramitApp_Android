package com.example.stramitapp

import com.example.stramitapp.model.DataObject.BaseDataObject
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/* tbl_licensee
CREATE TABLE "tbl_licensee" (
"licensee_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
"user_id" INT NULL DEFAULT NULL,
"numbers_of_users" INT NULL DEFAULT NULL,
*/

@Entity(tableName = "tbl_licensee")
data class Licensee(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "licensee_id")
    var licenseeId: Int = 0,

    @ColumnInfo(name = "user_id")
    var userId: Int? = null,

    @ColumnInfo(name = "numbers_of_users")
    var numberOfUsers: Int? = null
) : BaseDataObject()