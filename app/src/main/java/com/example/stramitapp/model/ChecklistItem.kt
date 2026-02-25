package com.example.stramitapp.models

import com.example.stramitapp.model.DataObject.BaseDataObject

class ChecklistItem : BaseDataObject() {
    var checklistItemId: Int = 0
    var checklistId: Int = 0
    var checklistItemName: String = ""
    var description: String = ""
    var isMandatory: Boolean = false
    var type: Boolean = false
    var isCaptureImage: Boolean = false
    var isEnterComments: Boolean = false
    var createdBy: Int = 0
    var updatedBy: Int = 0
    var updateFlag: String = ""
    var rangeStart: Int = 0
    var rangeEnd: Int = 0
    var lastUpdateDate: String = ""
}