package com.example.stramitapp.repositories

import com.example.stramitapp.dao.ReaderDeviceDao
import com.example.stramitapp.model.ReaderDevice
import com.example.stramitapp.repositories.Base.BaseRepository
import com.example.stramitapp.repositories.Base.IDataStore

abstract class ReaderDataStore(
    private val dao: ReaderDeviceDao
) : BaseRepository<ReaderDevice>(), IDataStore<ReaderDevice> {

   override suspend fun getItemAsync(id: Int): ReaderDevice? {
        return dao.getItem(id)
    }

   override suspend fun addItemAsync(item: ReaderDevice): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

   override suspend fun updateItemAsync(item: ReaderDevice): Boolean {
        return try {
            dao.update(item) == 1
        } catch (ex: Exception) {
            false
        }
    }

   override suspend fun deleteItemAsync(item: ReaderDevice): Boolean {
        return try {
            dao.delete(item) == 1
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<ReaderDevice> {
        initializeAsync()
        return dao.getAll()
    }

    suspend fun getItemsByTableAsync(idTable: Int): List<ReaderDevice> {
        return dao.getByTable(idTable)
    }

    override suspend fun initializeAsync() {
    }

    override suspend fun pullLatestAsync(): Boolean {
        throw NotImplementedError()
    }

    override suspend fun syncAsync(): Boolean {
        throw NotImplementedError()
    }
}