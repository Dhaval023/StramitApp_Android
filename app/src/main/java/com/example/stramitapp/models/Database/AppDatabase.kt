package com.example.stramitapp.models.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.stramitapp.dao.*
import com.example.stramitapp.model.*
import com.example.stramitapp.models.*
import com.example.stramitapp.utilities.AppSettings
import java.io.File

@Database(
    entities = [
        Company::class,
        Asset::class,
        AssetCondition::class,
        AssetFinancialInfo::class,
        AssetInspectionInfo::class,
        AssetInsuranceInfo::class,
        AssetIssueImages::class,
        AssetMovementInfo::class,
        AssetMaintenanceInfo::class,
        AssetMemoInfo::class,
        AssetLeaseInfo::class,
        WpCompany::class,
        CompanyLocation::class,
        AssetStatus::class,
        User::class,
        Shipment::class,
        Application::class,
        CompanyAssetSubParts::class,
        JobDescription::class,
        JobType::class,
        DeviceType::class,
        LicenseePeer::class,
        Licensee::class,
        LicenseeConfig::class,
        MobileJob::class,
        RoleToRightsMapping::class,
        UserRole::class,
        Rights::class,
        Role::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun companyDao(): CompanyDao
    abstract fun assetConditionDao(): AssetConditionDao
    abstract fun assetDao(): AssetDao
    abstract fun assetFinancialInfoDao(): AssetFinancialInfoDao
    abstract fun assetInspectionInfoDao(): AssetInspectionInfoDao
    abstract fun assetInsuranceInfoDao(): AssetInsuranceInfoDao
    abstract fun assetIssueImagesDao(): AssetIssueImagesDao
    abstract fun assetMovementInfoDao(): AssetMovementInfoDao
    abstract fun assetMemoInfoDao(): AssetMemoInfoDao
    abstract fun assetMaintenanceInfoDao(): AssetMaintenanceInfoDao
    abstract fun assetLeaseInfoDao(): AssetLeaseInfoDao
    abstract fun companyLocationDao(): CompanyLocationDao
    abstract fun userDao(): UserDao
    abstract fun wpCompanyDao(): WpCompanyDao
    abstract fun assetStatusDao(): AssetStatusDao
    abstract fun shipmentDao(): ShipmentDao
    abstract fun applicationDao(): ApplicationDataDao
    abstract fun deviceTypeDao(): DeviceTypeDao
    abstract fun companyAssetSubPartsDao(): CompanyAssetSubPartsDao
    abstract fun jobDescriptionDao(): JobDescriptionDao
    abstract fun jobTypeDao(): JobTypeDao
    abstract fun licenseeConfigDao(): LicenseeConfigDao
    abstract fun licenseeDao(): LicenseeDao
    abstract fun licenseePeerDao(): LicenseePeerDao
    abstract fun mobileJobDao(): MobileJobDao
    abstract fun rightsDao(): RightsDao
    abstract fun roleDao(): RoleDao
    abstract fun roleToRightsMappingDao(): RoleToRightsMappingDao
    abstract fun userRoleDao(): UserRoleDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun init(context: Context) {
            synchronized(this) {
                if (INSTANCE == null || INSTANCE?.isOpen == false) {
                    INSTANCE = build(context)
                }
            }
        }

        fun resetInstance() {
            synchronized(this) {
                try {
                    INSTANCE?.openHelper?.close()
                } catch (_: Exception) {}
                INSTANCE = null
            }
        }

        fun getInstance(): AppDatabase {
            return INSTANCE?.takeIf { it.isOpen }
                ?: synchronized(this) {
                    INSTANCE?.takeIf { it.isOpen }
                        ?: build(AppSettings.appContext).also { INSTANCE = it }
                }
        }

        private fun build(context: Context): AppDatabase {
            val dbFile = File(
                AppSettings.pathDatabase,
                AppSettings.databaseName
            )
            dbFile.parentFile?.mkdirs()

            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                dbFile.absolutePath
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}