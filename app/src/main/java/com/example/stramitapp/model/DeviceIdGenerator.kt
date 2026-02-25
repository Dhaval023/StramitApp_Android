package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class DeviceIdGenerator : BaseDataObject() {
    var deviceId: Int = 0
    var deviceType: String = ""
    var currentDeviceUdid: String = ""
    var deviceTokenId: String = ""
}