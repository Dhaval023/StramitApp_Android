package com.example.stramitapp.repositories

import com.example.stramitapp.dao.BillOfMaterialDao
import com.example.stramitapp.model.BillOfMaterial
import com.example.stramitapp.repositories.Base.BaseRepository
import com.example.stramitapp.repositories.Base.IDataStore

class BillOfMaterialDataStore(
    private val dao: BillOfMaterialDao
) : BaseRepository<BillOfMaterial>(), IDataStore<BillOfMaterial> {

    override suspend fun getItemAsync(id: Int): BillOfMaterial? {
        return try {
            dao.getItem(id)
        } catch (ex: Exception) {
            throw ex
        }
    }

    override suspend fun addItemAsync(item: BillOfMaterial): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun updateItemAsync(item: BillOfMaterial): Boolean {
        return try {
            val rows = dao.update(item)
            rows == 1
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: BillOfMaterial): Boolean {
        return try {
            val rows = dao.delete(item)
            rows == 1
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<BillOfMaterial> {
        return dao.getAll()
    }

    suspend fun getItemsToExportAsync(lastSyncUpData: String): List<BillOfMaterial> {
        return dao.getItemsToExport(lastSyncUpData)
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