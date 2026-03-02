package com.example.stramitapp.Repositories

import com.example.stramitapp.model.AttributePreferences
import com.example.stramitapp.model.MixAttributePreferences
import com.example.stramitapp.Repositories.Base.BaseRepository
import com.example.stramitapp.Repositories.Base.IDataStore
//
//class AttributePreferenceDataStore : BaseRepository<AttributePreferences>(), IDataStore<AttributePreferences> {
//
//    suspend fun getItemAsync(id: Int): AttributePreferences? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<AttributePreferences>().firstOrNull { item -> item.id == id } }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: AttributePreferences): Boolean {
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
//    suspend fun updateItemAsync(item: AttributePreferences): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                val rowsAffected = it.update(item)
//                rowsAffected == 1
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            false
//        }
//    }
//
//    suspend fun deleteItemAsync(item: AttributePreferences): Boolean {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                val rowsAffected = it.delete(item)
//                rowsAffected == 1
//            }
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
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<AttributePreferences> {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<AttributePreferences>() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getAttributes(companyId: Int, tableName: String): List<MixAttributePreferences> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                val sql = """
//                    SELECT company_id as companyId, p.table_id as tableId, table_name as tableName,
//                           attribute_id as attributeId, column_name as attributeName,
//                           substr(visible_mandatory, 1,1) as Visible,
//                           substr(visible_mandatory, 2,1) as Required,
//                           p.custom_name as customName
//                    FROM tbl_attribute_preferences p
//                    INNER JOIN tbl_attribute a ON a.table_id = p.table_id AND a.id = p.attribute_id
//                    INNER JOIN tbl_attribute_table t ON t.id = a.table_id
//                    WHERE company_id = $companyId AND table_name = '$tableName'
//                    ORDER BY column_name
//                """.trimIndent()
//                it.rawQuery(sql)
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsByTableCompanyAsync(idTable: Int, idCompany: Int): List<AttributePreferences> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<AttributePreferences>()
//                    .filter { item -> item.tableId == idTable && item.companyId == idCompany }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsByAttributesAsync(idTable: Int, idCompany: Int, lstAttributes: List<Int>): List<AttributePreferences> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<AttributePreferences>()
//                    .filter { item ->
//                        item.tableId == idTable &&
//                                item.companyId == idCompany &&
//                                lstAttributes.contains(item.attributeId)
//                    }
//            }
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
//}