package com.example.stramitapp.model.WebService

import androidx.lifecycle.MutableLiveData
import com.example.stramitapp.model.DataObject.BaseDataObject
import java.util.Date

class DeviceDetailsModel : BaseDataObject() {

    // "665a94ee535e97d4"
    val currentDeviceUdid = MutableLiveData<String>("")

    // "APA91bEQDTvGiUy91yTQy4EjhZ8XXsiA2gigz67j01ggZsPOrSlNp7XnNPLu0uo40o9QpLP4maO4_dHeOfX2jgs0HuHS5QfiwFouAPE3lqhDadnA5RVdybB5r9AQj_EsFRSUZc55lZsF"
    val deviceTokenId = MutableLiveData<String>("")

    // "Android"
    val deviceType = MutableLiveData<String>("")

    // 3601
    val deviceId = MutableLiveData<Int?>(null)

    // "1.3.0"
    val synVersion = MutableLiveData<String>("")

    // 2017-02-18 08:45:46.906
    val lastSyncData = MutableLiveData<Date?>(null)
}