package com.example.stramitapp.Repositories

import android.util.Log
import com.example.stramitapp.model.AssetIssueImages
import com.example.stramitapp.model.AssetIssueInfo
import com.example.stramitapp.Repositories.Base.BaseRepository
//import com.example.stramitapp.Utilities.AppSettings
//import com.example.stramitapp.Utilities.ApiSettings
//import com.example.stramitapp.Services.API.APIHelper
import com.example.stramitapp.Repositories.Base.IDataStore
import java.io.File
import java.time.LocalDate
//
//class AssetIssueInfoDataStore : BaseRepository<AssetIssueInfo>(), IDataStore<AssetIssueInfo> {
//
//    suspend fun getItemAsync(id: Int): AssetIssueInfo? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<AssetIssueInfo>().firstOrNull { item -> item.id == id } }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getNewIdAsync(): Int {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<AssetIssueInfo>().maxOfOrNull { item -> item.id + 1 } ?: 0
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: AssetIssueInfo): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use { it.insert(item) }
//            true
//        } catch (ex: Exception) {
//            val d = ex.message
//            false
//        }
//    }
//
//    // TODO: ASSET ISSUE AND ASSET RETURN
//    suspend fun addAssetIssue(items: List<Any>): Boolean {
//        var ret = true
//        val conn = getConnection()
//        conn.beginTransaction()
//        try {
//            val newId = getNewIdAsync()
//            var newIdAux = 0
//            val dtUpdate = APIHelper.nowDateTimeSQLite()
//
//            val newIdImg = App.repository.assetIssueImagesDataStore.getNewIdAsync()
//            var newIdImgAux = 0
//
//            for (item in items) {
//                val aIdSelected = 0
//                val aDeviceIdSelected = 0
//                val aReturnDateVM = LocalDate.now().toString()
//                val aDescriptionVM = ""
//                val aConditionDestinationVM = 0
//                val aUserDestinationName = ""
//                val aUserDestinationVM: Int? = null
//                val aNameImageAsset = ""
//                val aNameImageSignature = ""
//                val aWorkOrderNumber = ""
//                val aCompanyDestinationVM: Int? = null
//                val aType = ""
//                val msg = "Asset Issue Failed"
//
//                val itemAII = AssetIssueInfo()
//                newIdAux = if (newIdAux == 0) newId else (newIdAux + 1)
//                itemAII.id = newIdAux
//                itemAII.assetId = aIdSelected
//                itemAII.deviceId = aDeviceIdSelected
//                itemAII.attributeDeviceId = AppSettings.authenticatedUser.currentDeviceType ?: 0
//                itemAII.type = aType
//                itemAII.issueTo = aUserDestinationVM
//                itemAII.issueToSupplierId = 0
//                itemAII.date = aReturnDateVM
//                itemAII.description = aDescriptionVM
//                itemAII.conditionId = aConditionDestinationVM
//                itemAII.name = aUserDestinationName
//                itemAII.updatedBy = AppSettings.authenticatedUser.userId
//                itemAII.updateFlag = "I"
//                itemAII.lastUpdateDate = dtUpdate
//                itemAII.flagSync = 0
//                itemAII.companyId = aCompanyDestinationVM
//                itemAII.workOrderNumber = aWorkOrderNumber
//
//                val km = conn.insert(itemAII)
//                if (0 < km) {
//                    if (aNameImageAsset.isNotEmpty()) {
//                        newIdImgAux = if (newIdImgAux == 0) newIdImg else (newIdImgAux + 1)
//                        val imgA = getAssetIssueImagesObj(newIdImgAux, newIdAux, aDeviceIdSelected, dtUpdate, aNameImageAsset, aType)
//                        val kia = conn.insert(imgA)
//                        if (kia == 0) throw Exception(msg)
//                    }
//                    if (aNameImageSignature.isNotEmpty()) {
//                        newIdImgAux = if (newIdImgAux == 0) newIdImg else (newIdImgAux + 1)
//                        val imgS = getAssetIssueImagesObj(newIdImgAux, newIdAux, aDeviceIdSelected, dtUpdate, aNameImageSignature, aType)
//                        val kis = conn.insert(imgS)
//                        if (kis == 0) throw Exception(msg)
//                    }
//                }
//            }
//            conn.commitTransaction()
//        } catch (ex: Exception) {
//            val dd = ex.message
//            conn.rollbackTransaction()
//            ret = false
//        }
//        return ret
//    }
//
//    private fun getAssetIssueImagesObj(
//        newIdImg: Int,
//        assetIssueId: Int,
//        deviceId: Int,
//        dtUpdate: String,
//        fileName: String,
//        type: String
//    ): AssetIssueImages {
//        try {
//            val servicePath = "getMM.do?fileName=/astrack/asset_issue_return/"
//            val imageType: String
//            val newFileName: String
//            val imagePath: String
//
//            if (fileName.startsWith("ir_sig_id")) {
//                imageType = "SING"
//                val urlImgSignature = "${ApiSettings.BASE_URL}${servicePath}signature/"
//                newFileName = "ir_sig_id${assetIssueId}_did${AppSettings.authenticatedUser.currentDeviceType}.jpeg"
//                imagePath = "$urlImgSignature$newFileName"
//            } else {
//                imageType = "IMG"
//                val urlImgIssue = "${ApiSettings.BASE_URL}$servicePath"
//                newFileName = "ir_img_id${assetIssueId}_did${AppSettings.authenticatedUser.currentDeviceType}_${deviceId}.jpeg"
//                imagePath = "$urlImgIssue$newFileName"
//            }
//
//            val pathType = if (type == "I") AppSettings.pathAssetIssueImages else AppSettings.pathAssetReturnImages
//            val filePath = File(pathType, fileName)
//            if (filePath.exists()) {
//                val filePathNewFileName = File(pathType, newFileName)
//                if (filePathNewFileName.exists()) filePathNewFileName.delete()
//                filePath.copyTo(filePathNewFileName)
//                filePath.delete()
//            }
//
//            return AssetIssueImages().apply {
//                id = newIdImg
//                this.assetIssueId = assetIssueId
//                this.type = "I"
//                this.imageType = imageType
//                this.imagePath = imagePath
//                updatedBy = AppSettings.authenticatedUser.userId
//                updateFlag = "I"
//                lastUpdateDate = dtUpdate
//                flagSync = 0
//                dTSMMSync = ""
//                sTDMMSync = dtUpdate
//                imageSync = "0"
//            }
//        } catch (ex: Exception) {
//            val msg = ex.message
//            throw Exception("Issue Asset Image Failed")
//        }
//    }
//
//    suspend fun updateItemAsync(item: AssetIssueInfo): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use { it.update(item) }
//            true
//        } catch (ex: Exception) {
//            val d = ex.message
//            false
//        }
//    }
//
//    suspend fun deleteItemAsync(item: AssetIssueInfo): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use { it.delete(item) }
//            true
//        } catch (ex: Exception) {
//            val d = ex.message
//            false
//        }
//    }
//
//    suspend fun clearAsync(): Boolean {
//        throw NotImplementedError("clearAsync is not implemented")
//    }
//
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<AssetIssueInfo> {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<AssetIssueInfo>() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<AssetIssueInfo> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                val query = "SELECT id, type, asset_id, device_id, date, issue_to, update_flag, last_update_date, flag_sync, updated_by, attribute_device_id, issue_to_supplierId, description, condition_id, name FROM tbl_asset_issue_info WHERE last_update_date > '$lastSyncUpData' AND updated_by = ${AppSettings.authenticatedUser.userId}"
//                it.rawQuery(query)
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun initializeAsync() {
//        throw NotImplementedError("initializeAsync is not implemented")
//    }
//
//    suspend fun pullLatestAsync(): Boolean {
//        throw NotImplementedError("pullLatestAsync is not implemented")
//    }
//
//    suspend fun syncAsync(): Boolean {
//        throw NotImplementedError("syncAsync is not implemented")
//    }
//}