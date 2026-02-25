package com.example.stramitapp.models.Constants

import android.content.Context
import android.content.SharedPreferences

object StorageKeys {

    private const val PREF_NAME = "stramit_prefs"

    // ─── Key Constants ────────────────────────────────────────────────
    private const val KEY_LICENSE           = "LicenseeKey"
    private const val KEY_REMEMBER          = "IsRememberCredentials"
    private const val KEY_LOGIN_ONLINE      = "IsLoginOnline"
    private const val KEY_USERNAME          = "LoggedInUsername"
    private const val KEY_PASSWORD          = "LoggedInPassword"
    private const val KEY_LAST_SYNC         = "LastSyncData"
    private const val KEY_LAST_SYNC_UP      = "LastSyncUpData"
    private const val KEY_LAST_SYNC_JOB     = "LastSyncUpJob"
    private const val KEY_SYSTEM            = "SelectedSystem"
    private const val KEY_COMPANY           = "SelectedCompany"
    private const val KEY_LOCATION          = "SelectedLocation"
    private const val KEY_FRESH_INSTALL     = "IsFreshInstall"
    private const val KEY_FORCE_SYNC        = "ForceSyncRequested"

    // ─── SharedPreferences Instance ──────────────────────────────────
    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // ─── License Key ─────────────────────────────────────────────────
    fun saveLicenseKey(context: Context, value: String) =
        prefs(context).edit().putString(KEY_LICENSE, value).apply()

    fun getLicenseKey(context: Context): String =
        prefs(context).getString(KEY_LICENSE, "") ?: ""

    fun clearLicenseKey(context: Context) =
        prefs(context).edit().remove(KEY_LICENSE).apply()

    // ─── Remember Credentials ────────────────────────────────────────
    fun saveRememberCredentials(context: Context, value: Boolean) =
        prefs(context).edit().putBoolean(KEY_REMEMBER, value).apply()

    fun getRememberCredentials(context: Context): Boolean =
        prefs(context).getBoolean(KEY_REMEMBER, true)

    // ─── Login Online ─────────────────────────────────────────────────
    fun saveLoginOnline(context: Context, value: Boolean) =
        prefs(context).edit().putBoolean(KEY_LOGIN_ONLINE, value).apply()

    fun getLoginOnline(context: Context): Boolean =
        prefs(context).getBoolean(KEY_LOGIN_ONLINE, true)

    // ─── Logged In User ───────────────────────────────────────────────
    fun saveUsername(context: Context, value: String) =
        prefs(context).edit().putString(KEY_USERNAME, value).apply()

    fun getUsername(context: Context): String =
        prefs(context).getString(KEY_USERNAME, "") ?: ""

    fun savePassword(context: Context, value: String) =
        prefs(context).edit().putString(KEY_PASSWORD, value).apply()

    fun getPassword(context: Context): String =
        prefs(context).getString(KEY_PASSWORD, "") ?: ""

    // ─── Sync Timestamps ──────────────────────────────────────────────
    fun saveLastSyncData(context: Context, value: String) =
        prefs(context).edit().putString(KEY_LAST_SYNC, value).apply()

    fun getLastSyncData(context: Context): String =
        prefs(context).getString(KEY_LAST_SYNC, "") ?: ""

    fun saveLastSyncUpData(context: Context, value: String) =
        prefs(context).edit().putString(KEY_LAST_SYNC_UP, value).apply()

    fun getLastSyncUpData(context: Context): String =
        prefs(context).getString(KEY_LAST_SYNC_UP, "") ?: ""

    fun saveLastSyncUpJob(context: Context, value: String) =
        prefs(context).edit().putString(KEY_LAST_SYNC_JOB, value).apply()

    fun getLastSyncUpJob(context: Context): String =
        prefs(context).getString(KEY_LAST_SYNC_JOB, "") ?: ""

    // ─── Selected Items ───────────────────────────────────────────────
    fun saveSelectedSystem(context: Context, value: String) =
        prefs(context).edit().putString(KEY_SYSTEM, value).apply()

    fun getSelectedSystem(context: Context): String =
        prefs(context).getString(KEY_SYSTEM, "") ?: ""

    fun saveSelectedCompany(context: Context, value: String) =
        prefs(context).edit().putString(KEY_COMPANY, value).apply()

    fun getSelectedCompany(context: Context): String =
        prefs(context).getString(KEY_COMPANY, "") ?: ""

    fun saveSelectedLocation(context: Context, value: String) =
        prefs(context).edit().putString(KEY_LOCATION, value).apply()

    fun getSelectedLocation(context: Context): String =
        prefs(context).getString(KEY_LOCATION, "") ?: ""

    // ─── Fresh Install ────────────────────────────────────────────────
    fun setFreshInstall(context: Context, value: Boolean) =
        prefs(context).edit().putBoolean(KEY_FRESH_INSTALL, value).apply()

    fun isFreshInstall(context: Context): Boolean =
        prefs(context).getBoolean(KEY_FRESH_INSTALL, true)

    // ─── Force Sync ───────────────────────────────────────────────────
    fun setForceSyncRequested(context: Context, value: Boolean) =
        prefs(context).edit().putBoolean(KEY_FORCE_SYNC, value).apply()

    fun isForceSyncRequested(context: Context): Boolean =
        prefs(context).getBoolean(KEY_FORCE_SYNC, false)

    // ─── Clear session on logout ──────────────────────────────────────
    fun clearSession(context: Context) {
        prefs(context).edit()
            .remove(KEY_USERNAME)
            .remove(KEY_PASSWORD)
            .remove(KEY_LOGIN_ONLINE)
            .apply()
    }

    // ─── Clear everything (fresh install / factory reset) ─────────────
    fun clearAll(context: Context) =
        prefs(context).edit().clear().apply()
}