package com.example.stramitapp.models.response

import com.google.gson.annotations.SerializedName

data class GetDeviceIdResponse(
    @SerializedName("Message")
    val message: String?,
    @SerializedName("Status")
    val status: Boolean?,
    @SerializedName("Data")
    val data: DeviceData?
)

data class DeviceData(
    @SerializedName("ID")
    val id: Int?,
    @SerializedName("DeviceID")
    val deviceId: String?,
    @SerializedName("Description")
    val description: String?,
    @SerializedName("IsActive")
    val isActive: Boolean?
)
