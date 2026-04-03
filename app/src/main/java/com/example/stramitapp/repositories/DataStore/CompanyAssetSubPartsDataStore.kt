package com.example.stramitapp.repositories

import android.util.Log
import com.example.stramitapp.dao.CompanyAssetSubPartsDao
import com.example.stramitapp.model.CompanyAssetSubParts
import com.example.stramitapp.repositories.Base.BaseRepository
import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.utilities.AppSettings

class CompanyAssetSubPartsDataStore :
    BaseRepository<CompanyAssetSubParts>(),
    IDataStore<CompanyAssetSubParts> {

    private val dao: CompanyAssetSubPartsDao
        get() = AppSettings.database.companyAssetSubPartsDao()

    override suspend fun getItemAsync(id: Int): CompanyAssetSubParts? {
        return dao.getById(id)
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<CompanyAssetSubParts> {
        return dao.getAll()
    }

    suspend fun getItemsByTypeAsync(typeId: Int): List<CompanyAssetSubParts> {
        return dao.getByType(typeId)
    }

    override suspend fun addItemAsync(item: CompanyAssetSubParts): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (e: Exception) {
            Log.e("SubPartsDS", "Insert failed", e)
            false
        }
    }

    override suspend fun updateItemAsync(item: CompanyAssetSubParts): Boolean {
        return try {
            dao.insert(item) // replace
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: CompanyAssetSubParts): Boolean {
        return try {
            dao.delete(item)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun clearAsync(): Boolean {
        return try {
            dao.clearAll()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun initializeAsync() {}
    override suspend fun pullLatestAsync(): Boolean = false
    override suspend fun syncAsync(): Boolean = false
}