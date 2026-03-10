package com.example.stramitapp.Repositories

import com.example.stramitapp.Dao.AssetMemoTypeDao
import com.example.stramitapp.Dao.AssetStatusDao
import com.example.stramitapp.model.AssetStatus
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore
import com.example.stramitapp.model.AssetMemoType

abstract class AssetStatusDataStore(
    private val dao: AssetStatusDao
) : IDataStore<AssetStatus> {

   override suspend fun getItemAsync(id: Int): AssetStatus? = dao.getItem(id)

    suspend fun getItemsAsync(): List<AssetStatus> = dao.getAll()

    suspend fun getItemsByCompanyAsync(companyId: Int): List<AssetStatus> =
        dao.getItemsByCompany(companyId)

    suspend fun getFirstItemByCompanyAsync(companyId: Int): AssetStatus? =
        dao.getFirstItemByCompany(companyId)

    override suspend fun addItemAsync(item: AssetStatus): Boolean =
        try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }

    override suspend fun updateItemAsync(item: AssetStatus): Boolean =
        try {
            dao.update(item)
            true
        } catch (ex: Exception) {
            false
        }

    override suspend fun deleteItemAsync(item: AssetStatus): Boolean =
        try {
            dao.delete(item)
            true
        } catch (ex: Exception) {
            false
        }

    override  suspend fun clearAsync(): Boolean =
        try {
            dao.clear()
            true
        } catch (ex: Exception) {
            false
        }
}