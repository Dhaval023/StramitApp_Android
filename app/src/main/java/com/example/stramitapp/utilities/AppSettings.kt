package com.example.stramitapp.utilities

import android.content.Context
import android.content.SharedPreferences
//import androidx.security.crypto.EncryptedSharedPreferences
//import androidx.security.crypto.MasterKey
import com.example.stramitapp.models.*
import com.example.stramitapp.models.Local.ReaderTypeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.stramitapp.model.*
import com.example.stramitapp.models.Constants.StorageKeys

// ─────────────────────────────────────────────
//  Secure Prefs helper (replaces CrossSecureStorage)
// ─────────────────────────────────────────────
object SecurePrefs {
    private lateinit var prefs: SharedPreferences

//    fun init(context: Context) {
//        val masterKey = MasterKey.Builder(context)
//            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
//            .build()
//
//        prefs = EncryptedSharedPreferences.create(
//            context,
//            "secure_app_prefs",
//            masterKey,
//            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//        )
//    }

    fun get(key: String): String? = prefs.getString(key, null)
    fun set(key: String, value: String) = prefs.edit().putString(key, value).apply()
    fun remove(key: String) = prefs.edit().remove(key).apply()
}

// ─────────────────────────────────────────────
//  AppSettings  (singleton object — thread-safe)
// ─────────────────────────────────────────────
object AppSettings {

    // ── Connectivity ──────────────────────────────────────────────────────────

    /** True when a network connection is available. Supply your own checker impl. */
//    var isOnline: Boolean = false
//        get() = ConnectionChecker.isConnected()   // replace with your impl
//
//    fun isBluetoothEnabled(): Boolean = BluetoothChecker.isEnabled()  // replace with your impl
//    fun setBluetoothStatus(enable: Boolean) = BluetoothChecker.setStatus(enable)
//
//    fun tryTurnOnBluetooth() {
//        if (!isBluetoothEnabled()) {
//            setBluetoothStatus(true)
//            Thread.sleep(2_000)
//        }
//    }

    // ── Error messages ────────────────────────────────────────────────────────

    const val READER_ERROR_MSG =
        "RFID Reader is not connected. Please go to Reader connection and re-connect the reader."

    // ── System paths / names ──────────────────────────────────────────────────

    var pathDatabase: String = ""
    var pathDownloads: String = ""
    var pathImages: String = ""
    var pathAssetNewImages: String = ""
    var pathAssetIssueImages: String = ""
    var pathAssetReturnImages: String = ""
    var databaseName: String = ""

    // ── Device info ───────────────────────────────────────────────────────────

    var deviceId: Int = 0
    var deviceUdid: String = ""
    var deviceType: String = ""
    var syncVersion: String = ""
    var configName: String = ""

    // ── App state flags ───────────────────────────────────────────────────────

    var isLoginOnline: Boolean = false
    var isAutoSync: Boolean = false
    var willContinueAutoSync: Boolean = false
    var syncInterval: Double = 0.0
    var isFreshInstall: String = ""
    var loginErrorMessage: String = ""

    // ── Reader type ───────────────────────────────────────────────────────────

    var isTSLReader: Boolean = false
    var isZebraReader: Boolean = false
    var isNoneReader: Boolean = false

    /** Callback invoked whenever [currentReaderType] changes. */
    var onReaderTypeChanged: ((ReaderTypeModel) -> Unit)? = null

    private var _currentReaderType: ReaderTypeModel = ReaderTypeModel.RFID

    var currentReaderType: ReaderTypeModel
        get() = _currentReaderType
        set(value) {
            if (_currentReaderType == value) return
            _currentReaderType = value
            onReaderTypeChanged?.invoke(value)
        }

    /** Convenience alias — setting this also updates [currentReaderType]. */
    var isRFIDReaderConnection: Boolean
        get() = currentReaderType == ReaderTypeModel.RFID
        set(value) {
            currentReaderType = if (value) ReaderTypeModel.RFID else ReaderTypeModel.BARCODE
        }

    var isNoneRFIDReaderConnection: Boolean
        get() = currentReaderType != ReaderTypeModel.RFID
        set(value) {
            currentReaderType = if (value) ReaderTypeModel.BARCODE else ReaderTypeModel.RFID
        }

    // ── Internal / auth ───────────────────────────────────────────────────────

    var isDoneSyncing: Boolean = false
    var licenseeKey: String = ""
    var authenticatedUser: User? = null
    var selectedSystem: Company? = null
    var selectedCompany: WpCompany? = null
    var selectedLocation: CompanyLocation? = null
    var lastSyncData: Long? = null          // store as epoch millis
    var lastSyncUpData: String = ""
    var lastSyncUpJob: String = ""

    /** Persisted securely across app launches. */
    var isForceSyncRequested: Boolean
        get() = SecurePrefs.get(StorageKeys.KEY_FORCE_SYNC)
            ?.lowercase() == "true"
        set(value) = SecurePrefs.set(StorageKeys.KEY_FORCE_SYNC, value.toString())

    // ── Temp / selected items ─────────────────────────────────────────────────

    var tempSelectedSystem: Company? = null
    var tempSelectedCompany: WpCompany? = null
    var tempSelectedLocation: CompanyLocation? = null
    var tempAssetSelectedLocation: CompanyLocation? = null
    var tempSelectedJobType: JobType? = null
    var tempSelectedAssetCat: CompanyAssetCategory? = null
    var tempSelectedCondition: AssetCondition? = null
    var tempSelectedDescription: JobDescription? = null
    var tempSelectedAssetType: CompanyAssetType? = null
    var tempSelectedAssetParts: CompanyAssetParts? = null
    var tempSelectedAssetModel: CompanyAssetSubParts? = null
    var tempSelectedAssetStatus: AssetStatus? = null
    var tempSelectedAssetCondition: AssetCondition? = null
    var tempMixAttribute: MixAttributePreferences? = null
    var jobNumberText: String = ""

    // ── Init helpers ──────────────────────────────────────────────────────────

    /**
     * Load persisted system / company / location selections from secure storage.
     * Must be called from a coroutine (e.g. `lifecycleScope.launch`).
     */
//    suspend fun initializeSettings(repository: Repository) = withContext(Dispatchers.IO) {
//        val systemId   = SecurePrefs.get(StorageKeys.KEY_SYSTEM)
//        val companyId  = SecurePrefs.get(StorageKeys.KEY_COMPANY)
//        val locationId = SecurePrefs.get(StorageKeys.KEY_LOCATION)
//
//        selectedSystem   = systemId?.toIntOrNull()
//            ?.let { repository.companyDataStore.getItem(it) }
//
//        selectedCompany  = companyId?.toIntOrNull()
//            ?.let { repository.wpCompanyDataStore.getItem(it) }
//
//        selectedLocation = locationId?.toIntOrNull()
//            ?.let { repository.companyLocationDataStore.getItem(it) }
//
//        syncVersion = "1.3.0"
//    }

    /**
     * Copy current selections into the Temp* fields and clear job-specific state.
     * Safe to call on any thread.
     */
    fun initializeSelectedItems() {
        tempSelectedSystem        = selectedSystem
        tempSelectedCompany       = selectedCompany
        tempSelectedLocation      = selectedLocation
        tempAssetSelectedLocation = selectedLocation
        tempSelectedJobType       = null
        tempSelectedAssetCat      = null
        tempSelectedCondition     = null
        tempSelectedDescription   = null
        tempSelectedAssetType     = null
        tempSelectedAssetParts    = null
        tempSelectedAssetModel    = null
        tempSelectedAssetStatus   = null
        tempSelectedAssetCondition = null
        jobNumberText             = ""
    }
}