package com.example.stramitapp.repositories.DataStore

//import com.example.stramitapp.Repositories.Base.BaseRepository
//
//class CompanyAssetSubPartsDataStore : BaseRepository<CompanyAssetSubParts>(), IDataStore<CompanyAssetSubParts> {
//
//    suspend fun getItemAsync(id: Int): CompanyAssetSubParts? {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<CompanyAssetSubParts>().firstOrNull { item -> item.subPartId == id } }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<CompanyAssetSubParts> {
//        return try {
//            val conn = getConnection()
//            conn.use { it.queryAll<CompanyAssetSubParts>() }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    // Function to fetch Models based on selected device
//    suspend fun getItemsByTypeAsync(typeId: Int): List<CompanyAssetSubParts> {
//        return try {
//            val conn = getConnection()
//            conn.use {
//                it.queryAll<CompanyAssetSubParts>()
//                    .filter { item -> item.partId == typeId && item.updateFlag != "D" }
//            }
//        } catch (ex: Exception) {
//            val d = ex.message
//            throw ex
//        }
//    }
//
//    suspend fun addItemAsync(item: CompanyAssetSubParts): Boolean {
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
//    suspend fun updateItemAsync(item: CompanyAssetSubParts): Boolean {
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
//    suspend fun deleteItemAsync(item: CompanyAssetSubParts): Boolean {
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