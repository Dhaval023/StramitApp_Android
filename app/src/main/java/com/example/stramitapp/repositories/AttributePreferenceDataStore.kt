package com.example.stramitapp.repositories

import com.example.stramitapp.dao.AttributePreferencesDao
import com.example.stramitapp.model.AttributePreferences
import com.example.stramitapp.model.MixAttributePreferences
import com.example.stramitapp.repositories.Base.BaseRepository
import com.example.stramitapp.repositories.Base.IDataStore

class AttributePreferenceDataStore(
    private val dao: AttributePreferencesDao
) : BaseRepository<AttributePreferences>(), IDataStore<AttributePreferences> {

    override suspend fun getItemAsync(id: Int): AttributePreferences? {
        return try {
            dao.getItem(id)
        } catch (ex: Exception) {
            val d = ex.message
            throw ex
        }
    }

    override suspend fun addItemAsync(item: AttributePreferences): Boolean {
        return try {
            dao.insert(item)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun updateItemAsync(item: AttributePreferences): Boolean {
        return try {
            val rows = dao.update(item)
            rows == 1
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun deleteItemAsync(item: AttributePreferences): Boolean {
        return try {
            val rows = dao.delete(item)
            rows == 1
        } catch (ex: Exception) {
            false
        }
    }

   override suspend fun clearAsync(): Boolean {
        throw NotImplementedError("clearAsync is not implemented")
    }

    override suspend fun getItemsAsync(forceRefresh: Boolean): List<AttributePreferences> {
        return dao.getAll()
    }

    suspend fun getAttributes(
        companyId: Int,
        tableName: String
    ): List<MixAttributePreferences> {
        return dao.getAttributes(companyId, tableName)
    }

    suspend fun getItemsByTableCompanyAsync(
        idTable: Int,
        idCompany: Int
    ): List<AttributePreferences> {
        return dao.getItemsByTableCompany(idTable, idCompany)
    }

    suspend fun getItemsByAttributesAsync(
        idTable: Int,
        idCompany: Int,
        lstAttributes: List<Int>
    ): List<AttributePreferences> {
        return dao.getItemsByAttributes(idTable, idCompany, lstAttributes)
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