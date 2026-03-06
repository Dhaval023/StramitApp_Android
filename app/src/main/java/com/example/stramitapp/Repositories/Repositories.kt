package com.example.stramitapp.Repositories

import com.example.stramitapp.Repositories.DataStore.WpCompanyDataStore

class Repository(
    val companyDataStore: CompanyDataStore,
    val wpCompanyDataStore: WpCompanyDataStore,
    val companyLocationDataStore: CompanyLocationDataStore,
    val assetDataStore: AssetDataStore,
    val assetMemoInfoDataStore : AssetMemoInfoDataStore,
    val assetMaintenanceInfoDataStore : AssetMaintenanceInfoDataStore
)