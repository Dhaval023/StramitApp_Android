package com.example.stramitapp.Repositories

import com.example.stramitapp.Dao.AttributeTableDao
import com.example.stramitapp.model.AttributeTable
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore

class AttributeTableDataStore(
    private val dao: AttributeTableDao
) : BaseRepository<AttributeTable>(), IDataStore<AttributeTable> {

    override suspend fun getItemAsync(id: Int): AttributeTable? {
        return try {
            dao.getItem(id)
        } catch (ex: Exception) {
            val d = ex.message
            throw ex
        }
    }

    suspend fun getItemByNameAsync(tableName: String): AttributeTable? {
        return try {
            dao.getItemByName(tableName)
        } catch (ex: Exception) {
            val d = ex.message
            throw ex
        }
    }

    override suspend fun addItemAsync(item: AttributeTable): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun updateItemAsync(item: AttributeTable): Boolean {
        return try {
            val rows = dao.update(item)
            rows == 1
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: AttributeTable): Boolean {
        return try {
            val rows = dao.delete(item)
            rows == 1
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun clearAsync(): Boolean {
        throw NotImplementedError("clearAsync not implemented")
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<AttributeTable> {
        return dao.getAll()
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