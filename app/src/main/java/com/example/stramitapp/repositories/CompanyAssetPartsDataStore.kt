package com.example.stramitapp.repositories

import com.example.stramitapp.dao.CompanyAssetPartsDao
import com.example.stramitapp.model.CompanyAssetParts
import com.example.stramitapp.repositories.Base.BaseRepository
import com.example.stramitapp.repositories.Base.IDataStore

class CompanyAssetPartsDataStore(
    private val dao: CompanyAssetPartsDao
) : BaseRepository<CompanyAssetParts>(), IDataStore<CompanyAssetParts> {

    override suspend fun getItemAsync(partId: Int): CompanyAssetParts? {
        return try {
            dao.getItem(partId)
        } catch (ex: Exception) {
            throw ex
        }
    }

    override suspend fun addItemAsync(item: CompanyAssetParts): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun updateItemAsync(item: CompanyAssetParts): Boolean {
        return try {
            val rows = dao.update(item)
            rows == 1
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: CompanyAssetParts): Boolean {
        return try {
            val rows = dao.delete(item)
            rows == 1
        } catch (ex: Exception) {
            false
        }
    }

    suspend fun clearAllAsync(): Int {
        return try {
            dao.clearAll()
        } catch (ex: Exception) {
            -1
        }
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<CompanyAssetParts> {
        return dao.getAll()
    }

    suspend fun getItemsAsyncByDevice(deviceId: Int): List<CompanyAssetParts> {
        return dao.getItemsByDevice(deviceId)
    }

    suspend fun getItemsAsync(companyId: Int): List<CompanyAssetParts> {
        return dao.getItemsByCompany(companyId)
    }

    override suspend fun clearAsync(): Boolean {
        throw NotImplementedError("clearAsync not implemented")
    }

    override suspend fun initializeAsync() {
        throw NotImplementedError("initializeAsync not implemented")
    }

    override suspend fun pullLatestAsync(): Boolean {
        throw NotImplementedError("pullLatestAsync not implemented")
    }

    override suspend fun syncAsync(): Boolean {
        throw NotImplementedError("syncAsync not implemented")
    }
}