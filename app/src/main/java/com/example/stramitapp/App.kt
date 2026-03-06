package com.example.stramitapp

import android.app.Application
import android.os.Build
import com.example.stramitapp.services.API.request.GetDeviceIdRequest
import android.os.Environment
import com.example.stramitapp.models.Database.AppDatabase
import com.example.stramitapp.utilities.AppSettings
import com.example.stramitapp.utilities.SecurePrefs
import com.google.firebase.BuildConfig
import java.io.File

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        SecurePrefs.init(this)
        setupAppSettings()          // ← set paths FIRST
        AppDatabase.init(this)      // ← init DB AFTER paths are set
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

        // DEBUG:   /sdcard/Download/TsTAsTrack2-3
        // RELEASE: /sdcard/Download/PrdAsTrack1-5
        val dbFolderName = if (BuildConfig.DEBUG) "TsTAsTrack2-3" else "PrdAsTrack1-5"

        AppSettings.pathDatabase  = "$downloadsFolder/$dbFolderName"
        AppSettings.pathDownloads = downloadsFolder
        AppSettings.pathImages    = imagesFolder

        // ── Create folders ────────────────────────────────────────────────────
        File(AppSettings.pathDatabase).mkdirs()

        val newImages    = File(imagesFolder, "AssetNewImages")
        val issueImages  = File(imagesFolder, "AssetIssueImages")
        val returnImages = File(imagesFolder, "AssetReturnImages")

        if (!newImages.exists())    newImages.mkdirs()
        if (!issueImages.exists())  issueImages.mkdirs()
        if (!returnImages.exists()) returnImages.mkdirs()

        AppSettings.pathAssetNewImages    = newImages.absolutePath    + "/"
        AppSettings.pathAssetIssueImages  = issueImages.absolutePath  + "/"
        AppSettings.pathAssetReturnImages = returnImages.absolutePath + "/"
    }

//    private fun getDeviceId(): String {
//        return android.provider.Settings.Secure.getString(
//            contentResolver,
//            android.provider.Settings.Secure.ANDROID_ID
//        ) ?: ""
//    }
}