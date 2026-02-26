package com.example.stramitapp.model.WebService

import com.example.stramitapp.model.DataObject.BaseDataObject

class Ws : BaseDataObject() {

    var statusCode: Int = 0
    var error: String = ""
    var success: String = ""
    var databaseTimeStamp: String = ""
    var userDetails: List<Any> = mutableListOf()
    var deviceInfoDetails: List<Any> = mutableListOf()
}