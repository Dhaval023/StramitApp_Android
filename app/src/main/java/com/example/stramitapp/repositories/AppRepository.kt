package com.example.stramitapp.repositories

class Repository(
    val companyDataStore: CompanyDataStore,
    val wpCompanyDataStore: com.example.stramitapp.repositories.DataStore.WpCompanyDataStore,
    val companyLocationDataStore: CompanyLocationDataStore,
    val assetDataStore: AssetDataStore,
    val assetMemoInfoDataStore : AssetMemoInfoDataStore,
    val assetMaintenanceInfoDataStore : AssetMaintenanceInfoDataStore,
    val assetMovementInfoDataStore:AssetMovementInfoDataStore,
    val userDataStore: UserDataStore
)
