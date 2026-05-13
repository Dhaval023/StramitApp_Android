package com.example.stramitapp

import android.app.Application
import android.os.Environment
import com.example.stramitapp.repositories.Repository
import com.example.stramitapp.models.Database.AppDatabase
import com.example.stramitapp.utilities.AppSettings
import com.example.stramitapp.utilities.SecurePrefs
import com.example.stramitapp.repositories.AssetDataStore
import com.example.stramitapp.repositories.AssetMaintenanceInfoDataStore
import com.example.stramitapp.repositories.AssetMemoInfoDataStore
import com.example.stramitapp.repositories.AssetMovementInfoDataStore
import com.example.stramitapp.repositories.CompanyLocationDataStore
import com.example.stramitapp.repositories.UserDataStore
import com.example.stramitapp.repositories.WpCompanyDataStore
import com.example.stramitapp.repositories.CompanyDataStore
import com.example.stramitapp.BuildConfig
import android.media.MediaScannerConnection
import android.util.Log
import java.io.File

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SecurePrefs.init(this)
    }

    fun initializeDatabase() {
        setupAppSettings()


        AppSettings.appContext = this

        AppDatabase.init(this)

        val db = AppDatabase.getInstance()

        buildRepository(db)
    }
    fun reinitializeRepository() {
        val db = AppDatabase.getInstance()
        buildRepository(db)
    }

    private fun buildRepository(db: AppDatabase) {
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
                wpCompanyDataStore = WpCompanyDataStore(),
                assetMovementInfoDataStore = AssetMovementInfoDataStore()
            )
    }

    private fun setupAppSettings() {

        AppSettings.databaseName = "st_astrack2_0.db"
        AppSettings.deviceType   = "Android"
        AppSettings.syncVersion  = "1.3.0"

        // Use the public Downloads directory so it's visible to the user
        val downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath

        val imagesFolder = getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath
            ?: filesDir.absolutePath

        val dbFolderName =
            if (BuildConfig.DEBUG) "TsTAsTrack2-3"
            else "PrdAsTrack1-5"

        AppSettings.pathDatabase  = "$downloadsFolder/$dbFolderName"
        AppSettings.pathDownloads = downloadsFolder
        AppSettings.pathImages    = imagesFolder

        val dbDir = File(AppSettings.pathDatabase)
        if (!dbDir.exists()) {
            dbDir.mkdirs()
        }

        val newImages    = File(imagesFolder, "AssetNewImages")
        val issueImages  = File(imagesFolder, "AssetIssueImages")
        val returnImages = File(imagesFolder, "AssetReturnImages")

        if (!newImages.exists()) newImages.mkdirs()
        if (!issueImages.exists()) issueImages.mkdirs()
        if (!returnImages.exists()) returnImages.mkdirs()

        AppSettings.pathAssetNewImages    = newImages.absolutePath + File.separator
        AppSettings.pathAssetIssueImages  = issueImages.absolutePath + File.separator
        AppSettings.pathAssetReturnImages = returnImages.absolutePath + File.separator

        // Refresh MediaScanner so the folder and files are visible to the user immediately
        MediaScannerConnection.scanFile(this, arrayOf(dbDir.absolutePath), null, null)
        Log.d("App", "Database path set to: ${AppSettings.pathDatabase}")
    }
}