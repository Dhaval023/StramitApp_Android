package com.example.stramitapp.repositories

import com.example.stramitapp.dao.CompanyAssetTypeDao
import com.example.stramitapp.model.CompanyAssetType
import com.example.stramitapp.repositories.Base.BaseRepository
import com.example.stramitapp.repositories.Base.IDataStore

class CompanyAssetTypeDataStore(
    private val dao: CompanyAssetTypeDao
) : BaseRepository<CompanyAssetType>(), IDataStore<CompanyAssetType> {

    override suspend fun getItemAsync(id: Int): CompanyAssetType? {
        return try {
            dao.getItem(id)
        } catch (ex: Exception) {
            throw ex
        }
    }

    override suspend fun addItemAsync(item: CompanyAssetType): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun updateItemAsync(item: CompanyAssetType): Boolean {
        return try {
            val rows = dao.update(item)
            rows == 1
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: CompanyAssetType): Boolean {
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

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<CompanyAssetType> {
        return dao.getAll()
    }

    suspend fun getItemsAsync(companyId: Int): List<CompanyAssetType> {
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