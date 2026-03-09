package com.example.stramitapp.model.DataObject

import androidx.room.Ignore
import com.example.stramitapp.models.Interfaces.Base.IBaseLocalModel
import com.google.gson.annotations.Expose
import java.time.OffsetDateTime
import java.util.UUID

open class BaseDataObject : ObservableObject(), IBaseLocalModel {

    @Ignore
    @Expose(serialize = false, deserialize = false)
    override open var id: Int = 0

    @Ignore
    var localId: String = UUID.randomUUID().toString()

    @Ignore
    var createdAt: OffsetDateTime? = null

    @Ignore
    var updatedAt: OffsetDateTime? = null

    @Ignore
    var azureVersion: String? = null
}
