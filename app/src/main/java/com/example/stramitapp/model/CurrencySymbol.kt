package com.example.stramitapp.model

import com.example.stramitapp.model.DataObject.BaseDataObject

class CurrencySymbol : BaseDataObject() {
    var id: Int = 0
    var currencySymbols: String = ""
    var name: String = ""
    var updateFlag: String = ""
    var lastUpdateDate: String = ""
}