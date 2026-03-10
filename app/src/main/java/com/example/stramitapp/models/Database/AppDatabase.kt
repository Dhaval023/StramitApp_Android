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
    version = 1,
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
        private const val DB_NAME = "astrack_ams.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun init(context: Context) {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        val dbFile = File(
                            AppSettings.pathDatabase,   // /sdcard/Download/TsTAsTrack2-3
                            AppSettings.databaseName    // st_astrack2_0.db
                        )
                        dbFile.parentFile?.mkdirs()

                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            dbFile.absolutePath         // ← full custom path
                        )
                            .fallbackToDestructiveMigration()
                            .build()

                    }
                }
            }
        }
                fun getInstance(): AppDatabase =
                    INSTANCE ?: throw IllegalStateException(
                        "AppDatabase not initialized. Call AppDatabase.init(context) in your Application.onCreate()."
                    )


    }
}