package com.example.stramitapp.common.API.FloorSweep.request

import com.google.gson.annotations.SerializedName

data class FloorSweepRequest(

    @SerializedName("deliveryDate")
    var deliveryDate: String? = null,

    @SerializedName("assetTag")
    var assetTag: List<String>? = null
)