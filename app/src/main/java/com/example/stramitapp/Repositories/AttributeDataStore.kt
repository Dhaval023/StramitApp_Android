package com.example.stramitapp.Repositories

import com.example.stramitapp.Dao.AttributesDao
import com.example.stramitapp.model.Attributes
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore

class AttributeDataStore(
    private val dao: AttributesDao
) : BaseRepository<Attributes>(), IDataStore<Attributes> {

    override suspend fun getItemAsync(id: Int): Attributes? {
        return try {
            dao.getItem(id)
        } catch (ex: Exception) {
            val d = ex.message
            throw ex
        }
    }

    override suspend fun addItemAsync(item: Attributes): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            val d = ex.message
            false
        }
    }

    override suspend fun updateItemAsync(item: Attributes): Boolean {
        return try {
            val rows = dao.update(item)
            rows == 1
        } catch (ex: Exception) {
            val d = ex.message
            false
        }
    }

    override suspend fun deleteItemAsync(item: Attributes): Boolean {
        return try {
            val rows = dao.delete(item)
            rows == 1
        } catch (ex: Exception) {
            val d = ex.message
            false
        }
    }

    override suspend fun clearAsync(): Boolean {
        throw NotImplementedError("clearAsync is not implemented")
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<Attributes> {
        return try {
            dao.getAll()
        } catch (ex: Exception) {
            val d = ex.message
            throw ex
        }
    }

    suspend fun getItemsByTableAsync(idTable: Int): List<Attributes> {
        return try {
            dao.getItemsByTable(idTable)
        } catch (ex: Exception) {
            val d = ex.message
            throw ex
        }
    }

    override suspend fun initializeAsync() {
        throw NotImplementedError("initializeAsync is not implemented")
    }

    override suspend fun pullLatestAsync(): Boolean {
        throw NotImplementedError("pullLatestAsync is not implemented")
    }

    override suspend fun syncAsync(): Boolean {
        throw NotImplementedError("syncAsync is not implemented")
    }
}