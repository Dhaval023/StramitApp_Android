package com.example.stramitapp.models.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.stramitapp.Dao.AssetConditionDao
import com.example.stramitapp.Dao.AssetDao
import com.example.stramitapp.Dao.CompanyDao
import com.example.stramitapp.Dao.AssetFinancialInfoDao
import com.example.stramitapp.Dao.AssetInspectionInfoDao
import com.example.stramitapp.Dao.AssetInsuranceInfoDao
import com.example.stramitapp.Dao.AssetIssueImagesDao
import com.example.stramitapp.Dao.AssetLeaseInfoDao
import com.example.stramitapp.Dao.AssetMovementInfoDao
import com.example.stramitapp.model.Asset
import com.example.stramitapp.model.User
import com.example.stramitapp.model.AssetCondition
import com.example.stramitapp.model.AssetFinancialInfo
import com.example.stramitapp.model.AssetInspectionInfo
import com.example.stramitapp.model.AssetInsuranceInfo
import com.example.stramitapp.model.AssetIssueImages
import com.example.stramitapp.model.AssetMovementInfo
import com.example.stramitapp.Dao.AssetMemoInfoDao
import com.example.stramitapp.Dao.AssetMaintenanceInfoDao
import com.example.stramitapp.Dao.AssetStatusDao
import com.example.stramitapp.Dao.CompanyLocationDao
import com.example.stramitapp.Dao.UserDao
import com.example.stramitapp.Dao.WpCompanyDao
import com.example.stramitapp.model.AssetMaintenanceInfo
import com.example.stramitapp.model.AssetMemoInfo
import com.example.stramitapp.model.AssetLeaseInfo
import com.example.stramitapp.model.AssetStatus
import com.example.stramitapp.model.Company
import com.example.stramitapp.models.WpCompany
import com.example.stramitapp.model.CompanyLocation
import com.example.stramitapp.model.Shipment
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
        Shipment::class
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

        // ── Auto-reinit if closed or null ─────────────────────────────────
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