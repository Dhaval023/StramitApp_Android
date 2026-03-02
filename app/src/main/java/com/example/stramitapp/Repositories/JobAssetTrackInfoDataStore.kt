package com.example.stramitapp.Repositories


import com.example.stramitapp.model.Asset
import com.example.stramitapp.model.AssetMovementInfo
import com.example.stramitapp.model.JobAssetTrackInfo
import com.example.stramitapp.Repositories.Base.BaseRepository
//import com.example.stramitapp.Services.API.APIHelper
//import com.example.stramitapp.Utilities.AppSettings
import com.example.stramitapp.Repositories.Base.IDataStore
//
//class JobAssetTrackInfoDataStore : BaseRepository<JobAssetTrackInfo>(), IDataStore<JobAssetTrackInfo> {
//
//    suspend fun getItemAsync(id: Int): JobAssetTrackInfo? {
//        throw NotImplementedError("getItemAsync(id) is not implemented")
//    }
//
//    suspend fun getItemByKeyAsync(jobId: Int, assetId: Int): JobAssetTrackInfo? {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<JobAssetTrackInfo>()
//                    .firstOrNull { item -> item.jobId == jobId && item.assetId == assetId }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsAsync(jobId: Int, assetId: Int, locationId: Int): JobAssetTrackInfo? {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<JobAssetTrackInfo>()
//                    .firstOrNull { item -> item.jobId == jobId && item.assetId == assetId && item.locationId == locationId }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsAsync(jobId: Int, locationId: Int): List<JobAssetTrackInfo> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<JobAssetTrackInfo>()
//                    .filter { item -> item.jobId == jobId && item.locationId == locationId }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsAsync(jobId: Int): List<JobAssetTrackInfo> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<JobAssetTrackInfo>().filter { item -> item.jobId == jobId }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun submitJob(
//        jobId: Int,
//        lstNew: List<Asset>? = null,
//        lstMove: List<Asset>? = null,
//        lstFound: List<Asset>? = null,
//        lstMissing: List<Asset>? = null
//    ): Boolean {
//        var ret = true
//        val conn = getConnection()
//        conn.beginTransaction()
//        try {
//            val dtUpdate = APIHelper.nowDateTimeSQLite()
//            val currentJob = App.repository.jobDefinitionDataStore.getItemAsync(jobId)
//
//            // Found
//            lstFound?.forEach { foundAsset ->
//                var retAssetTrack: Int
//                val foundTrack = getItemByKeyAsync(currentJob!!.jobId, foundAsset.assetId)
//                retAssetTrack = if (foundTrack == null) {
//                    conn.insert(JobAssetTrackInfo().apply {
//                        this.jobId = currentJob.jobId
//                        this.assetId = foundAsset.assetId
//                        deviceId = foundAsset.deviceId
//                        assetConditionId = foundAsset.conditionId
//                        trackDate = dtUpdate
//                        trackBy = AppSettings.authenticatedUser.userId
//                        assetImage = foundAsset.assetImage
//                        this.locationId = currentJob.locationId
//                        assetTrackStatus = APIHelper.AssetTrackStatus.Found.value.toShort()
//                        updateFlag = "I"
//                        lastUpdateDate = dtUpdate
//                        flagStatus = 0
//                        jobPerformedLocId = 0
//                    })
//                } else {
//                    foundTrack.trackBy = AppSettings.authenticatedUser.userId
//                    foundTrack.assetTrackStatus = APIHelper.AssetTrackStatus.Found.value.toShort()
//                    foundTrack.updateFlag = "U"
//                    foundTrack.lastUpdateDate = dtUpdate
//                    conn.insertOrReplace(foundTrack)
//                }
//                if (retAssetTrack == 0) throw Exception("Add Asset Track Failed")
//            }
//
//            // Missing
//            lstMissing?.forEach { notYetAsset ->
//                var retAssetTrack: Int
//                val notYetTrack = getItemByKeyAsync(currentJob!!.jobId, notYetAsset.assetId)
//                retAssetTrack = if (notYetTrack == null) {
//                    conn.insert(JobAssetTrackInfo().apply {
//                        this.jobId = currentJob.jobId
//                        assetId = notYetAsset.assetId
//                        deviceId = notYetAsset.deviceId
//                        assetConditionId = notYetAsset.conditionId
//                        trackDate = dtUpdate
//                        trackBy = AppSettings.authenticatedUser.userId
//                        assetImage = notYetAsset.assetImage
//                        locationId = currentJob.locationId
//                        assetTrackStatus = APIHelper.AssetTrackStatus.Missing.value.toShort()
//                        updateFlag = "I"
//                        lastUpdateDate = dtUpdate
//                        flagStatus = 0
//                        jobPerformedLocId = 0
//                    })
//                } else {
//                    notYetTrack.trackBy = AppSettings.authenticatedUser.userId
//                    notYetTrack.assetTrackStatus = APIHelper.AssetTrackStatus.Missing.value.toShort()
//                    notYetTrack.updateFlag = "U"
//                    notYetTrack.lastUpdateDate = dtUpdate
//                    conn.insertOrReplace(notYetTrack)
//                }
//                if (retAssetTrack == 0) throw Exception("Add Asset Track Failed")
//            }
//
//            // Move
//            lstMove?.forEach { moveAsset ->
//                var retAssetTrack: Int
//                val moveTrack = getItemByKeyAsync(currentJob!!.jobId, moveAsset.assetId)
//                retAssetTrack = if (moveTrack == null) {
//                    conn.insert(JobAssetTrackInfo().apply {
//                        this.jobId = currentJob.jobId
//                        assetId = moveAsset.assetId
//                        deviceId = moveAsset.deviceId
//                        assetConditionId = moveAsset.conditionId
//                        trackDate = dtUpdate
//                        trackBy = AppSettings.authenticatedUser.userId
//                        assetImage = moveAsset.assetImage
//                        locationId = currentJob.locationId
//                        assetTrackStatus = APIHelper.AssetTrackStatus.Move.value.toShort()
//                        updateFlag = "I"
//                        lastUpdateDate = dtUpdate
//                        flagStatus = 0
//                        jobPerformedLocId = 0
//                    })
//                } else {
//                    moveTrack.trackBy = AppSettings.authenticatedUser.userId
//                    moveTrack.assetTrackStatus = APIHelper.AssetTrackStatus.Move.value.toShort()
//                    moveTrack.updateFlag = "U"
//                    moveTrack.lastUpdateDate = dtUpdate
//                    conn.insertOrReplace(moveTrack)
//                }
//                if (retAssetTrack == 0) throw Exception("Add Asset Track Failed")
//
//                val km = conn.insert(AssetMovementInfo().apply {
//                    assetId = moveAsset.assetId
//                    deviceId = moveAsset.deviceId
//                    attributeDeviceId = AppSettings.authenticatedUser.currentDeviceType ?: 0
//                    sourceLocationId = moveAsset.locationId!!
//                    destinationLocationId = currentJob!!.locationId!!
//                    movementDate = dtUpdate
//                    movedBy = AppSettings.authenticatedUser.userId
//                    movementType = "in"
//                    movementRecordedBy = AppSettings.authenticatedUser.userId
//                    updatedBy = AppSettings.authenticatedUser.userId
//                    updateFlag = "I"
//                    lastUpdateDate = dtUpdate
//                    flagSync = 0
//                    workOrderNumber = ""
//                })
//                if (km > 0) {
//                    val resAsset = App.repository.assetDataStore.getItemAsync(moveAsset.assetId, moveAsset.deviceId)
//                        ?: throw Exception("Update Asset Failed")
//                    resAsset.locationId = currentJob!!.locationId
//                    resAsset.companyId = currentJob.companyId
//                    resAsset.lastUpdateDeviceId = AppSettings.deviceId
//                    resAsset.lastUpdateDate = dtUpdate
//                    resAsset.updateFlag = "U"
//                    resAsset.lastUpdatedBy = AppSettings.authenticatedUser.userId
//                    if (conn.insertOrReplace(resAsset) == 0) throw Exception("Update Asset Failed")
//                } else {
//                    throw Exception("Add Movement Failed")
//                }
//            }
//
//            // New
//            lstNew?.forEach { newAsset ->
//                val itemA = Asset().apply {
//                    assetId = newAsset.assetId
//                    deviceId = newAsset.deviceId
//                    lastUpdateDeviceId = AppSettings.deviceId
//                    companyAssetId = newAsset.companyAssetId
//                    assetType = newAsset.assetType
//                    title = newAsset.title
//                    tag = newAsset.tag
//                    barcode = newAsset.barcode
//                    serialNumber = newAsset.serialNumber
//                    assetValue = newAsset.assetValue
//                    partId = newAsset.partId
//                    locationId = newAsset.locationId
//                    categoryId = newAsset.categoryId
//                    statusId = newAsset.statusId
//                    companyId = newAsset.companyId
//                    conditionId = newAsset.conditionId
//                    longDesc = newAsset.longDesc
//                    gpsLat = 0.0
//                    gpsLong = 0.0
//                    assetImage = newAsset.assetImage
//                    createDate = dtUpdate
//                    purchaseDate = newAsset.purchaseDate
//                    lastUpdateDate = dtUpdate
//                    responsibleUserId = newAsset.responsibleUserId
//                    lastUpdatedBy = AppSettings.authenticatedUser.userId
//                    updateFlag = "I"
//                    dTSMMSync = "0"
//                    sTDMMSync = "0"
//                    flagSync = 0
//                    imageSync = 0
//                    weight = newAsset.weight
//                    weightUom = 0
//                }
//                val asset = conn.insert(itemA)
//                if (asset > 0) {
//                    val jTrack = conn.insert(JobAssetTrackInfo().apply {
//                        jobId = currentJob!!.jobId
//                        assetId = newAsset.assetId
//                        deviceId = newAsset.deviceId
//                        assetConditionId = newAsset.conditionId
//                        trackDate = dtUpdate
//                        trackBy = AppSettings.authenticatedUser.userId
//                        assetImage = newAsset.assetImage
//                        locationId = currentJob.locationId
//                        assetTrackStatus = APIHelper.AssetTrackStatus.New.value.toShort()
//                        updateFlag = "I"
//                        lastUpdateDate = dtUpdate
//                        flagStatus = 0
//                        jobPerformedLocId = 0
//                    })
//                    if (jTrack == 0) throw Exception("Add Asset Track Failed")
//                } else {
//                    throw Exception("Add Asset Failed")
//                }
//            }
//
//            conn.commitTransaction()
//            APIHelper().changeJobStatus(jobId, APIHelper.StatusJob.Completed)
//        } catch (ex: Exception) {
//            val dd = ex.message
//            conn.rollbackTransaction()
//            ret = false
//        }
//        return ret
//    }
//
//    fun syncDeviceServer(jobId: Int) {
//        // TODO: implement sync logic using coroutines
//        throw Exception("Sync Failed. Go and try to Sync Manually on Settings page.")
//    }
//
//    suspend fun addItemAsync(item: JobAssetTrackInfo): Boolean {
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
//    suspend fun updateItemAsync(item: JobAssetTrackInfo): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use { it.insertOrReplace(item) }
//            true
//        } catch (ex: Exception) {
//            val d = ex.message
//            false
//        }
//    }
//
//    suspend fun deleteItemAsync(item: JobAssetTrackInfo): Boolean {
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
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<JobAssetTrackInfo> {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<JobAssetTrackInfo>() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<JobAssetTrackInfo> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.rawQuery("SELECT * FROM tbl_job_asset_track_info WHERE last_update_date > '$lastSyncUpData'")
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsToExportAsync(lastSyncUpData: String, jobId: Int): List<JobAssetTrackInfo> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.rawQuery("SELECT * FROM tbl_job_asset_track_info WHERE job_id = $jobId AND last_update_date > '$lastSyncUpData'")
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