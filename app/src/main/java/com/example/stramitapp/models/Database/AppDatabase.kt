package com.example.stramitapp.models.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.stramitapp.Repositories.CompanyDao
import com.example.stramitapp.model.Company


@Database(
    entities = [
        Company::class
        // Add more entities here as you convert them e.g:
        // WpCompany::class,
        // CompanyLocation::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun companyDao(): CompanyDao
    // Add more DAOs here as you convert them e.g:
    // abstract fun wpCompanyDao(): WpCompanyDao
    // abstract fun companyLocationDao(): CompanyLocationDao

    companion object {
        private const val DB_NAME = "astrack_ams.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun init(context: Context) {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DB_NAME
                    )
                        .fallbackToDestructiveMigration() // swap for a Migration strategy in production
                        .build()
                }
            }
        }

        fun getInstance(): AppDatabase =
            INSTANCE ?: throw IllegalStateException(
                "AppDatabase not initialized. Call AppDatabase.init(context) in your Application.onCreate()."
            )
    }
}