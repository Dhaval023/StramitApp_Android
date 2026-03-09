package com.example.stramitapp.Repositories

import com.example.stramitapp.Dao.CompanyLocationDao
import com.example.stramitapp.model.CompanyLocation
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore

class CompanyLocationDataStore : BaseRepository<CompanyLocation>(), IDataStore<CompanyLocation> {

    private val dao: CompanyLocationDao by lazy { db.companyLocationDao() }

    override suspend fun getItemAsync(idLocation: Int): CompanyLocation? {
        return dao.getItem(idLocation)
    }

    suspend fun getItemByCompanyAsync(companyId: Int): List<CompanyLocation> {
        return dao.getItemByCompany(companyId)
    }

    suspend fun getParentLocationAsync(companyId: Int, locationId: Int): List<CompanyLocation> {
        return dao.getParentLocation(companyId, locationId)
    }

    suspend fun getItemByCompanyAsync(companyId: Int, userId: Int): List<CompanyLocation> {
        return dao.getItemByCompanyUser(companyId, userId)
    }

    suspend fun getLocationByScan(companyId: Int, barcode: String): CompanyLocation? {
        return dao.getLocationByScan(companyId, barcode)
    }

    override suspend fun addItemAsync(item: CompanyLocation): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun updateItemAsync(item: CompanyLocation): Boolean {
        return try {
            dao.update(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: CompanyLocation): Boolean {
        return try {
            dao.delete(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<CompanyLocation> {
        return dao.getAll()
    }

    // CompanyLocationDao has no clearAll() — delete-all via getAll() + delete each,
    // or just return true as a no-op if clearing locations is not needed
    override suspend fun clearAsync(): Boolean {
        return try {
            dao.getAll().forEach { dao.delete(it) }
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun initializeAsync() {}

    override suspend fun pullLatestAsync(): Boolean = false

    override suspend fun syncAsync(): Boolean = false
}