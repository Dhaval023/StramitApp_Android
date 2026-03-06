package com.example.stramitapp.model

import com.example.stramitapp.model.DataObject.BaseDataObject

class Log : BaseDataObject() {
  override  var id: Int = 0
    var title: String = ""
    var msg: String = ""
}