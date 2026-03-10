package com.example.stramitapp.Repositories

import com.example.stramitapp.Dao.CompanyAssetCategoryDao
import com.example.stramitapp.model.CompanyAssetCategory
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore

class CompanyAssetCategoryDataStore(
    private val dao: CompanyAssetCategoryDao
) : BaseRepository<CompanyAssetCategory>(), IDataStore<CompanyAssetCategory> {

    suspend fun getItemsAsync(companyId: Int): List<CompanyAssetCategory> {
        return try {
            dao.getItemsByCompany(companyId)
        } catch (ex: Exception) {
            throw ex
        }
    }

    override suspend fun getItemAsync(id: Int): CompanyAssetCategory? {
        return try {
            dao.getItem(id)
        } catch (ex: Exception) {
            throw ex
        }
    }

    override suspend fun addItemAsync(item: CompanyAssetCategory): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun updateItemAsync(item: CompanyAssetCategory): Boolean {
        return try {
            val rows = dao.update(item)
            rows == 1
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: CompanyAssetCategory): Boolean {
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

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<CompanyAssetCategory> {
        return dao.getAll()
    }

    suspend fun getFirstItemByCompanyAsync(companyId: Int): CompanyAssetCategory? {
        return dao.getFirstItemByCompany(companyId)
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