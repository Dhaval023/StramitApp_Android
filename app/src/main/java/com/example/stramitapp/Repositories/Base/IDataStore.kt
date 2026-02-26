package com.example.stramitapp.Repositories.Base

interface IDataStore<T> {

    suspend fun addItemAsync(item: T): Boolean
    suspend fun updateItemAsync(item: T): Boolean
    suspend fun deleteItemAsync(item: T): Boolean
    suspend fun clearAsync(): Boolean
    suspend fun getItemAsync(id: Int): T
    suspend fun getItemsAsync(forceRefresh: Boolean = false): List<T>
    suspend fun initializeAsync()
    suspend fun pullLatestAsync(): Boolean
    suspend fun syncAsync(): Boolean
}