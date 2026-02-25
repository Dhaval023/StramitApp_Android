package com.example.stramitapp.model.DataObject

import androidx.room.Ignore
import com.example.stramitapp.models.Interfaces.Base.IBaseLocalModel
import java.time.OffsetDateTime
import java.util.UUID

open class BaseDataObject : ObservableObject(), IBaseLocalModel {

    @Ignore
    var localId: String = UUID.randomUUID().toString()

    @Ignore
    var createdAt: OffsetDateTime? = null

    @Ignore
    var updatedAt: OffsetDateTime? = null

    @Ignore
    var azureVersion: String? = null
}