package com.example.stramitapp.repositories

import com.example.stramitapp.repositories.Base.IDataStore
import com.example.stramitapp.model.Company
import kotlinx.coroutines.Dispatchers
import com.example.stramitapp.repositories.Base.BaseRepository
import kotlinx.coroutines.withContext

class CompanyDataStore : BaseRepository<Company>(), IDataStore<Company> {

    override suspend fun getItemAsync(id: Int): Company? = withContext(Dispatchers.IO) {
        runCatching { db.companyDao().getById(id) }.getOrElse { throw it }
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<Company> = withContext(Dispatchers.IO) {
        runCatching { db.companyDao().getAll() }.getOrElse { throw it }
    }

    override suspend fun addItemAsync(item: Company): Boolean = withContext(Dispatchers.IO) {
        runCatching { db.companyDao().insert(item); true }.getOrElse { false }
    }

    override suspend fun updateItemAsync(item: Company): Boolean = withContext(Dispatchers.IO) {
        runCatching { db.companyDao().update(item); true }.getOrElse { false }
    }

    override suspend fun deleteItemAsync(item: Company): Boolean = withContext(Dispatchers.IO) {
        runCatching { db.companyDao().delete(item); true }.getOrElse { false }
    }

    override suspend fun clearAsync(): Boolean = withContext(Dispatchers.IO) {
        runCatching { db.companyDao().deleteAll(); true }.getOrElse { false }
    }

    override suspend fun initializeAsync() {}

    override suspend fun pullLatestAsync(): Boolean = false

    override suspend fun syncAsync(): Boolean = false

    suspend fun getFirstCompany(): Company? = withContext(Dispatchers.IO) {
        runCatching { db.companyDao().getFirst() }.getOrElse { throw it }
    }

    suspend fun resetCompany(idCompanyKeep: List<Int>): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            db.companyDao().getAll()
                .filter { it.companyId !in idCompanyKeep }
                .forEach { db.companyDao().deleteById(it.companyId) }
            true
        }.getOrElse { false }
    }

    suspend fun clearAll(): Int = withContext(Dispatchers.IO) {
        runCatching { db.companyDao().deleteAll() }.getOrElse { -1 }
    }

     suspend fun clear(): Boolean =
        throw NotImplementedError("clear() is not implemented")

    suspend fun initialize(): Nothing =
        throw NotImplementedError("initialize() is not implemented")

     suspend fun pullLatest(): Boolean =
        throw NotImplementedError("pullLatest() is not implemented")

     suspend fun sync(): Boolean =
        throw NotImplementedError("sync() is not implemented")
}