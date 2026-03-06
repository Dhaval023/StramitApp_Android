package com.example.stramitapp.Repositories

import androidx.room.withTransaction
import com.example.stramitapp.Dao.AssetIssueImagesDao
import com.example.stramitapp.Dao.AssetIssueInfoDao
import com.example.stramitapp.models.Database.AppDatabase
import com.example.stramitapp.model.AssetIssueInfo
import com.example.stramitapp.Repositories.Base.IDataStore
import com.example.stramitapp.services.APIHelper
import com.example.stramitapp.utilities.AppSettings

class AssetIssueInfoDataStore(
    private val db: AppDatabase,
    private val issueDao: AssetIssueInfoDao,
    private val imageDao: AssetIssueImagesDao
) : IDataStore<AssetIssueInfo> {

    override suspend fun getItemAsync(id: Int): AssetIssueInfo? {
        return issueDao.getById(id)
    }

    suspend fun getNewIdAsync(): Int {
        return issueDao.getNextId() ?: 1
    }

    override suspend fun addItemAsync(item: AssetIssueInfo): Boolean {
        return try {
            issueDao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    // ---------------- TRANSACTION ----------------

    suspend fun addAssetIssue(items: List<AssetIssueInfo>): Boolean {

        return try {

            db.withTransaction {

                val newId = getNewIdAsync()
                var newIdAux = 0
                val dtUpdate = APIHelper.nowDateTimeSQLite()

                val newIdImg = imageDao.getNextId() ?: 1

                for (item in items) {

                    newIdAux = if (newIdAux == 0) newId else newIdAux + 1

                    item.id = newIdAux
                    item.attributeDeviceId =
                        AppSettings.authenticatedUser?.currentDeviceType ?: 0

                    item.updatedBy =
                        AppSettings.authenticatedUser!!.userId

                    item.updateFlag = "I"
                    item.lastUpdateDate = dtUpdate
                    item.flagSync = 0

                    issueDao.insert(item)

                    // IMAGE INSERT CAN BE ADDED HERE
                }
            }

            true

        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun updateItemAsync(item: AssetIssueInfo): Boolean {
        return try {
            issueDao.update(item) > 0
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: AssetIssueInfo): Boolean {
        return try {
            issueDao.delete(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun clearAsync(): Boolean {
        return try {
            issueDao.clearAll()
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<AssetIssueInfo> {
        return issueDao.getAll()
    }

    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<AssetIssueInfo> {

        val userId = AppSettings.authenticatedUser?.userId ?: 0

        return issueDao.getItemsToExport(
            lastSyncUpData,
            userId
        )
    }

    override suspend fun initializeAsync() {}

    override suspend fun pullLatestAsync(): Boolean {
        return false
    }

    override suspend fun syncAsync(): Boolean {
        return false
    }
}