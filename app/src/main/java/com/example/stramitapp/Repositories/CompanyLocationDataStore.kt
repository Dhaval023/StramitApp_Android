package com.example.stramitapp.Repositories


import com.example.stramitapp.model.CompanyLocation
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore
//
abstract class CompanyLocationDataStore : BaseRepository<CompanyLocation>(), IDataStore<CompanyLocation> {
    abstract fun getItem(it: Int): CompanyLocation?
//
//    suspend fun getItemAsync(idLocation: Int): CompanyLocation? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<CompanyLocation>().firstOrNull { item -> item.locationId == idLocation } }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getLocationParentName(companyId: Int, locationId: Int, userId: Int?): String? {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                val userCondition = if (userId != null) " AND responsible_user_id = $userId" else ""
//                val sql = """
//                    WITH RECURSIVE LocationParentName AS (
//                        SELECT Location_id, Parent_Location_id, responsible_user_id, Location_name
//                        FROM tbl_company_location
//                        WHERE Parent_Location_id = 0 AND company_id = $companyId
//                        UNION
//                        SELECT l.Location_id, l.Parent_Location_id, l.responsible_user_id,
//                               t.Location_name || ' / ' || l.Location_name as LocationName
//                        FROM LocationParentName as t
//                        INNER JOIN tbl_company_location l ON t.Location_id = l.Parent_Location_id
//                        WHERE company_id = $companyId
//                    )
//                    SELECT Parent_Location_id, Location_id, Location_name, responsible_user_id
//                    FROM LocationParentName
//                    WHERE Location_id = $locationId$userCondition;
//                """.trimIndent()
//                it.rawQuery<CompanyLocation>(sql).firstOrNull()?.locationName
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    fun getItemByCompany(idCompany: Int, barcode: String?): List<CompanyLocation> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                val all = it.queryAll<CompanyLocation>()
//                if (barcode.isNullOrEmpty()) {
//                    all.filter { item -> item.companyId == idCompany && item.updateFlag != "D" }
//                } else {
//                    all.filter { item ->
//                        item.companyId == idCompany &&
//                                (item.tag.contains(barcode) || item.locationBarcode.contains(barcode)) &&
//                                item.updateFlag != "D"
//                    }
//                }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    fun getLocationByScan(idCompany: Int, barcode: String): CompanyLocation? {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<CompanyLocation>()
//                    .firstOrNull { item -> item.companyId == idCompany && item.locationBarcode == barcode }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemByCompanyAsync(idCompany: Int): List<CompanyLocation> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<CompanyLocation>()
//                    .filter { item -> item.companyId == idCompany && item.updateFlag != "D" }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getParentLocationAsync(idCompany: Int, locationId: Int): List<CompanyLocation> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<CompanyLocation>()
//                    .filter { item ->
//                        item.companyId == idCompany &&
//                                (item.locationId == locationId || item.parentLocationId == locationId)
//                    }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemByCompanyAsync(idCompany: Int, userId: Int): List<CompanyLocation> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<CompanyLocation>()
//                    .filter { item -> item.companyId == idCompany && item.responsibleUserId == userId }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: CompanyLocation): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use { it.insert(item) }
//            true
//        } catch (ex: Exception) {
//            val d = ex.message
//            false
//        }
//    }
//
//    suspend fun updateItemAsync(item: CompanyLocation): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use { it.update(item) }
//            true
//        } catch (ex: Exception) {
//            val d = ex.message
//            false
//        }
//    }
//
//    suspend fun deleteItemAsync(item: CompanyLocation): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use { it.delete(item) }
//            true
//        } catch (ex: Exception) {
//            val d = ex.message
//            false
//        }
//    }
//
//    suspend fun clearAsync(): Boolean {
//        throw NotImplementedError("clearAsync is not implemented")
//    }
//
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<CompanyLocation> {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<CompanyLocation>() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun initializeAsync() {
//        throw NotImplementedError("initializeAsync is not implemented")
//    }
//
//    suspend fun pullLatestAsync(): Boolean {
//        throw NotImplementedError("pullLatestAsync is not implemented")
//    }
//
//    suspend fun syncAsync(): Boolean {
//        throw NotImplementedError("syncAsync is not implemented")
//    }
}