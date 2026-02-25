package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(
    tableName = "tbl_attribute_preferences",
    indices = [Index(value = ["attribute_id", "company_id"], unique = true)]
)
class AttributePreferences : BaseDataObject() {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "attribute_id")
    var attributeId: Int = 0

    @ColumnInfo(name = "table_id")
    var tableId: Int = 0

    @ColumnInfo(name = "company_id")
    var companyId: Int = 0

    @ColumnInfo(name = "visible_mandatory")
    var visibleMandatory: Int = 0

    @ColumnInfo(name = "update_flag")
    var updateFlag: String? = null

    @ColumnInfo(name = "last_update_date")
    var lastUpdateDate: String? = null

    @ColumnInfo(name = "flag_sync")
    var flagSync: Int? = null

    @ColumnInfo(name = "custom_name")
    var customName: String? = null
}

class MixAttributePreferences {
    var companyId: Int = 0
    var tableId: Int = 0
    var tableName: String? = null
    var attributeId: Int = 0
    var attributeName: String? = null
    var customName: String? = null
    var visible: Int = 0
    var required: Int = 0
}