package com.example.stramitapp.services.API.response

import com.google.gson.annotations.SerializedName

data class FloorSweepResponse(

    @SerializedName("statusCode")
    var statusCode: Int = 0,

    @SerializedName("error")
    var error: String? = null,

    @SerializedName("success")
    var success: String? = null,

    @SerializedName("databaseTimeStamp")
    var databaseTimeStamp: Any? = null,

    @SerializedName("list")
    var list: List<ResultListModel>? = null
)
data class ResultListModel(

    @SerializedName("id")
    var id: String? = null,

    @SerializedName("co")
    var co: String? = null,

    @SerializedName("rfid")
    var rfid: String? = null,

    @SerializedName("barcode")
    var barcode: String? = null,

    @SerializedName("deliveryDate")
    var deliveryDate: String? = null,

    @SerializedName("productSKU")
    var productSKU: String? = null
)