package com.example.stramitapp.Repositories.DataStore//package com.example.stramitapp.Repositories.DataStore
//
//import android.util.Log
//import android.database.sqlite.SQLiteDatabase
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
//class ApplicationDataStore : BaseRepository<Application>(), IDataStore<Application> {
//
//    private val TAG = "ApplicationDataStore"
//
//    // ─────────────────────────────────────────────────────────────────────────
//    // Get Single Item
//    // ─────────────────────────────────────────────────────────────────────────
//
//    override suspend fun getItemAsync(id: Int): Application = withContext(Dispatchers.IO) {
//        try {
//            val db = SQLiteDatabase.openDatabase(localDatabase, null, SQLiteDatabase.OPEN_READONLY)
//            db.use {
//                val cursor = it.query(
//                    "Application",
//                    null,
//                    "AppId = ?",
//                    arrayOf(id.toString()),
//                    null, null, null
//                )
//                cursor.use { c ->
//                    if (c.moveToFirst()) c.toApplication() else throw Exception("Item not found")
//                }
//            }
//        } catch (ex: Exception) {
//            Log.e(TAG, "getItemAsync error: ${ex.message}", ex)
//            throw ex
//        }
//    }
//
//    // ─────────────────────────────────────────────────────────────────────────
//    // Get All Items
//    // ─────────────────────────────────────────────────────────────────────────
//
//    override suspend fun getItemsAsync(forceRefresh: Boolean): List<Application> =
//        withContext(Dispatchers.IO) {
//            try {
//                val db = SQLiteDatabase.openDatabase(localDatabase, null, SQLiteDatabase.OPEN_READONLY)
//                db.use {
//                    val cursor = it.query("Application", null, null, null, null, null, null)
//                    cursor.use { c ->
//                        val list = mutableListOf<Application>()
//                        while (c.moveToNext()) list.add(c.toApplication())
//                        list
//                    }
//                }
//            } catch (ex: Exception) {
//                Log.e(TAG, "getItemsAsync error: ${ex.message}", ex)
//                throw ex
//            }
//        }
//
//    // ─────────────────────────────────────────────────────────────────────────
//    // Add Item
//    // ─────────────────────────────────────────────────────────────────────────
//
//    override suspend fun addItemAsync(item: Application): Boolean = withContext(Dispatchers.IO) {
//        try {
//            val db = SQLiteDatabase.openDatabase(localDatabase, null, SQLiteDatabase.OPEN_READWRITE)
//            db.use {
//                it.insert("Application", null, item.toContentValues())
//            }
//            true
//        } catch (ex: Exception) {
//            Log.e(TAG, "addItemAsync error: ${ex.message}", ex)
//            false
//        }
//    }
//
//    // ─────────────────────────────────────────────────────────────────────────
//    // Update Item
//    // ─────────────────────────────────────────────────────────────────────────
//
//    override suspend fun updateItemAsync(item: Application): Boolean = withContext(Dispatchers.IO) {
//        try {
//            val db = SQLiteDatabase.openDatabase(localDatabase, null, SQLiteDatabase.OPEN_READWRITE)
//            db.use {
//                it.update(
//                    "Application",
//                    item.toContentValues(),
//                    "AppId = ?",
//                    arrayOf(item.appId.toString())
//                )
//            }
//            true
//        } catch (ex: Exception) {
//            Log.e(TAG, "updateItemAsync error: ${ex.message}", ex)
//            false
//        }
//    }
//
//    // ─────────────────────────────────────────────────────────────────────────
//    // Delete Item
//    // ─────────────────────────────────────────────────────────────────────────
//
//    override suspend fun deleteItemAsync(item: Application): Boolean = withContext(Dispatchers.IO) {
//        try {
//            val db = SQLiteDatabase.openDatabase(localDatabase, null, SQLiteDatabase.OPEN_READWRITE)
//            db.use {
//                it.delete("Application", "AppId = ?", arrayOf(item.appId.toString()))
//            }
//            true
//        } catch (ex: Exception) {
//            Log.e(TAG, "deleteItemAsync error: ${ex.message}", ex)
//            false
//        }
//    }
//
//    // ─────────────────────────────────────────────────────────────────────────
//    // Not Implemented
//    // ─────────────────────────────────────────────────────────────────────────
//
//    override suspend fun clearAsync(): Boolean = throw NotImplementedError()
//
//    override suspend fun initializeAsync() = throw NotImplementedError()
//
//    override suspend fun pullLatestAsync(): Boolean = throw NotImplementedError()
//
//    override suspend fun syncAsync(): Boolean = throw NotImplementedError()
//}