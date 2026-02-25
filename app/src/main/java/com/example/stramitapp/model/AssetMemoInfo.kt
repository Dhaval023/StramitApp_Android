package com.example.stramitapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stramitapp.model.DataObject.BaseDataObject

@Entity(tableName = "tbl_asset_memo_info")
class AssetMemoInfo : BaseDataObject() {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "asset_id")
    var assetId: Int = 0

    @ColumnInfo(name = "device_id")
    var deviceId: Int = 0

    @ColumnInfo(name = "last_update_device_id")
    var lastUpdateDeviceId: Int = 0

    @ColumnInfo(name = "memo_device_id")
    var memoDeviceId: Int = 0

    @ColumnInfo(name = "memo_date")
    var memoDate: String? = null

    @ColumnInfo(name = "memo_by")
    var memoBy: Int? = null

    @ColumnInfo(name = "note")
    var note: String? = null

    @ColumnInfo(name = "memo_type_id")
    var memoTypeId: Int = 0

    @ColumnInfo(name = "memo_audio_file")
    var memoAudioFile: String? = null

    @ColumnInfo(name = "memo_doc_file")
    var memoDocFile: String? = null

    @ColumnInfo(name = "memo_image_file")
    var memoImageFile: String? = null

    @ColumnInfo(name = "memo_video_file")
    var memoVideoFile: String? = null

    @ColumnInfo(name = "updated_by")
    var updateBy: Int = 0

    @ColumnInfo(name = "update_flag")
    var updateFlag: String? = null

    @ColumnInfo(name = "last_update_date")
    var lastUpdateDate: String? = null

    @ColumnInfo(name = "flag_sync")
    var flagSync: Int = 0

    @ColumnInfo(name = "image_sync")
    var imageSync: Int = 0

    @ColumnInfo(name = "DTS_a_mm_sync")
    var dTSAMMSync: String? = null

    @ColumnInfo(name = "DTS_d_mm_sync")
    var dTSDMMSync: String? = null

    @ColumnInfo(name = "DTS_i_mm_sync")
    var dTSIMMSync: String? = null

    @ColumnInfo(name = "DTS_v_mm_sync")
    var dTSVMMSync: String? = null

    @ColumnInfo(name = "STD_a_mm_sync")
    var sTDAMMSync: String? = null

    @ColumnInfo(name = "STD_d_mm_sync")
    var sTDDMMSync: String? = null

    @ColumnInfo(name = "STD_i_mm_sync")
    var sTDIMMSync: String? = null

    @ColumnInfo(name = "STD_v_mm_sync")
    var sTDVMMSync: String? = null
}