package com.example.stramitapp.model.WebService

import com.example.stramitapp.model.DataObject.BaseDataObject

class GetLoginDetails : BaseDataObject() {

    var loginIdUser: Int = 0
    var firstName: String = ""
    var loginName: String = ""
    var password: String = ""
    var currentDeviceType: Int? = null
    var device: DeviceDetailsModel = DeviceDetailsModel()
    // "6UENB3PAA8H9GGI90MGB"
    var licenseeKey: String = ""
    var licenseeId: Int = 0
    var isRememberLicenseeKey: Boolean = false
    var isLoginOnline: Boolean = false
    var isRememberCredentials: Boolean = false
    var apiAddress: String = ""
    var apiPort: String = ""
}