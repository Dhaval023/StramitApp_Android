package com.example.stramitapp.services.localdatastore

import com.example.stramitapp.Repositories.Base.IDataStore
import com.example.stramitapp.Repositories.BaseRepository
import com.example.stramitapp.model.Company
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CompanyDataStore : BaseRepository<Company>(), IDataStore<Company> {

    // ── Read ──────────────────────────────────────────────────────────────────

    suspend fun getItem(id: Int): Company? = withContext(Dispatchers.IO) {
        runCatching {
            db.companyDao().getById(id)
        }.getOrElse { e ->
            throw e
        }
    }

    suspend fun getFirstCompany(): Company? = withContext(Dispatchers.IO) {
        runCatching {
            db.companyDao().getFirst()
        }.getOrElse { e ->
            throw e
        }
    }

    override suspend fun getItems(forceRefresh: Boolean): List<Company> = withContext(Dispatchers.IO) {
        runCatching {
            db.companyDao().getAll()
        }.getOrElse { e ->
            throw e
        }
    }

    // ── Write ─────────────────────────────────────────────────────────────────

    override suspend fun addItem(item: Company): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            db.companyDao().insert(item)
            true
        }.getOrElse { false }
    }

    override suspend fun updateItem(item: Company): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            db.companyDao().update(item)
            true
        }.getOrElse { false }
    }

    override suspend fun deleteItem(item: Company): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            db.companyDao().delete(item)
            true
        }.getOrElse { false }
    }

    // ── Bulk operations ───────────────────────────────────────────────────────

    /**
     * Deletes all companies whose companyId is NOT in [idCompanyKeep].
     * Returns true on success, false on failure.
     */
    suspend fun resetCompany(idCompanyKeep: List<Int>): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            val toDelete = db.companyDao().getAll()
                .filter { it.companyId !in idCompanyKeep }

            toDelete.forEach { company ->
                db.companyDao().deleteById(company.companyId)
            }
            true
        }.getOrElse { false }
    }

    /**
     * Deletes every Company row.
     * Returns the number of rows deleted, or -1 on error.
     */
    suspend fun clearAll(): Int = withContext(Dispatchers.IO) {
        runCatching {
            db.companyDao().deleteAll()
        }.getOrElse { -1 }
    }

    // ── Not implemented (stubs kept for interface compliance) ─────────────────

    override suspend fun clear(): Boolean =
        throw NotImplementedError("clear() is not implemented")

    override suspend fun initialize() =
        throw NotImplementedError("initialize() is not implemented")

    override suspend fun pullLatest(): Boolean =
        throw NotImplementedError("pullLatest() is not implemented")

    override suspend fun sync(): Boolean =
        throw NotImplementedError("sync() is not implemented")
}