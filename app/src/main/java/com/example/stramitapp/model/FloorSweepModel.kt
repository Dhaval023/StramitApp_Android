package com.example.stramitapp.model

import com.google.gson.annotations.SerializedName

data class FloorSweepModel(
    @SerializedName("rfid")
    var rfid: String = ""
)

data class FloorSweepResultListModel(
    @SerializedName("id")
    var id: String = "",

    @SerializedName("co")
    var co: String = "",

    @SerializedName("rfid")
    var rfid: String = "",

    @SerializedName("deliveryDate")
    var deliveryDate: String = "",

    @SerializedName("productSKU")
    var productSKU: String = ""
)
