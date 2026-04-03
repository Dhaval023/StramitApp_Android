package com.example.stramitapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_licensee_peer")
class LicenseePeer (
    @PrimaryKey(autoGenerate = true)
    var peerId: Int = 0,
    override var id: Int = 0,
    var licenseeId: Int = 0,
    var userId: Int = 0
): BaseDataObject()