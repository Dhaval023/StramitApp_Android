package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class ProjectAssetExtra : BaseDataObject() {
    var projectMapId: Int = 0
    var projectId: Int = 0
    var assetId: Int = 0
    var groupNumber: String = ""
}