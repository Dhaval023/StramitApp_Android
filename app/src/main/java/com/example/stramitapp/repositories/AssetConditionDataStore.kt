package com.example.stramitapp.repositories//package com.example.stramitapp.repositories.datastore // lowercase
//
//import com.example.stramitapp.Repositories.Base.IDataStore
//import com.example.stramitapp.Repositories.Base.BaseRepository
//import com.example.stramitapp.model.Asset
//import com.example.stramitapp.model.AssetCondition
//import com.example.stramitapp.Dao.AssetConditionDao
//import com.example.stramitapp.Dao.AssetDao
//
//class AssetConditionDataStore : BaseRepository<AssetCondition>(), IDataStore<AssetCondition> {
//
//    private val dao: AssetConditionDao by lazy { db.assetConditionDao() }
//    private val assetDao: AssetDao by lazy { db.assetDao() }
//
//    override suspend fun getItemAsync(id: Int): AssetCondition {
//        return dao.getById(id)
//            ?: throw NoSuchElementException("AssetCondition with id $id not found")
//    }
//
//    override suspend fun addItemAsync(item: AssetCondition): Boolean {
//        return try {
//            dao.insert(item)
//            true
//        } catch (ex: Exception) {
//            false
//        }
//    }
//
//    override suspend fun updateItemAsync(item: AssetCondition): Boolean {
//        return try {
//            dao.update(item) > 0
//        } catch (ex: Exception) {
//            false
//        }
//    }
//
//    override suspend fun deleteItemAsync(item: AssetCondition): Boolean {
//        return try {
//            dao.delete(item)
//            true
//        } catch (ex: Exception) {
//            false
//        }
//    }
//
//    override suspend fun clearAsync(): Boolean {
//        return try {
//            dao.clearAll()
//            true
//        } catch (ex: Exception) {
//            false
//        }
//    }
//
//    override suspend fun getItemsAsync(forceRefresh: Boolean): List<AssetCondition> {
//        return dao.getAll()
//    }
//
//    override suspend fun initializeAsync() {
//        // Initialize logic here if needed
//    }
//
//    override suspend fun pullLatestAsync(): Boolean {
//        throw NotImplementedError("pullLatestAsync not implemented")
//    }
//
//    override suspend fun syncAsync(): Boolean {
//        throw NotImplementedError("syncAsync not implemented")
//    }
//
//    // Custom methods
//    suspend fun getItemsByCompanyAsync(companyId: Int): List<AssetCondition> {
//        return dao.getByCompanyId(companyId)
//    }
//
//    suspend fun getFirstItemByCompanyAsync(companyId: Int): AssetCondition? {
//        return dao.getFirstByCompanyId(companyId)
//    }
//
//    suspend fun getItemsByAssetAsyncStatus(assetUpdateFlag: String): List<Asset> {
//        return assetDao.getByUpdateFlag(assetUpdateFlag)
//    }
//}