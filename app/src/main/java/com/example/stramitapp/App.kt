package com.example.stramitapp

import android.app.Application
import android.os.Build
import android.os.Environment
import com.example.stramitapp.Repositories.Repository
import com.example.stramitapp.models.Database.AppDatabase
import com.example.stramitapp.utilities.AppSettings
import com.example.stramitapp.utilities.SecurePrefs
import com.example.stramitapp.Repositories.AssetDataStore
import com.example.stramitapp.Repositories.AssetMaintenanceInfoDataStore
import com.example.stramitapp.Repositories.AssetMemoInfoDataStore
import com.example.stramitapp.Repositories.CompanyLocationDataStore
import com.example.stramitapp.Repositories.UserDataStore
import com.example.stramitapp.Repositories.DataStore.WpCompanyDataStore
import com.example.stramitapp.Repositories.CompanyDataStore
import com.google.firebase.BuildConfig
import java.io.File

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        SecurePrefs.init(this)

//        setupAppSettings()          // set paths FIRST
//        AppDatabase.init(this)      // init DB AFTER paths are set
    }

    fun initializeDatabase() {
        setupAppSettings()
        AppDatabase.init(this)

        val db = AppDatabase.getInstance()
        AppSettings.database = db

        com.example.stramitapp.services.App.repository =
            Repository(
                assetDataStore = AssetDataStore(),
                assetMaintenanceInfoDataStore = AssetMaintenanceInfoDataStore(
                    dao = db.assetMaintenanceInfoDao()
                ),
                assetMemoInfoDataStore = AssetMemoInfoDataStore(
                    dao = db.assetMemoInfoDao()
                ),
                companyLocationDataStore = CompanyLocationDataStore(),
                companyDataStore = CompanyDataStore(),
                userDataStore = UserDataStore(),
                wpCompanyDataStore = WpCompanyDataStore()
            )
    }

    private fun setupAppSettings() {

        AppSettings.databaseName = "st_astrack2_0.db"
//        AppSettings.deviceUdid   = getDeviceId()
        AppSettings.deviceType   = "Android"
        AppSettings.syncVersion  = "1.3.0"

        val downloadsFolder = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        ).absolutePath

        val imagesFolder = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        ).absolutePath

        val dbFolderName =
            if (BuildConfig.DEBUG) "TsTAsTrack2-3"
            else "PrdAsTrack1-5"

        AppSettings.pathDatabase  = "$downloadsFolder/$dbFolderName"
        AppSettings.pathDownloads = downloadsFolder
        AppSettings.pathImages    = imagesFolder

        File(AppSettings.pathDatabase).mkdirs()

        val newImages    = File(imagesFolder, "AssetNewImages")
        val issueImages  = File(imagesFolder, "AssetIssueImages")
        val returnImages = File(imagesFolder, "AssetReturnImages")

        if (!newImages.exists()) newImages.mkdirs()
        if (!issueImages.exists()) issueImages.mkdirs()
        if (!returnImages.exists()) returnImages.mkdirs()

        AppSettings.pathAssetNewImages    = newImages.absolutePath + "/"
        AppSettings.pathAssetIssueImages  = issueImages.absolutePath + "/"
        AppSettings.pathAssetReturnImages = returnImages.absolutePath + "/"
    }

//    private fun getDeviceId(): String {
//        return android.provider.Settings.Secure.getString(
//            contentResolver,
//            android.provider.Settings.Secure.ANDROID_ID
//        ) ?: ""
//    }
}