package com.example.stramitapp.Repositories.Base

import android.content.Context
import com.example.stramitapp.models.Database.AppDatabase
import com.example.stramitapp.models.Interfaces.Base.IBaseLocalModel
import com.example.stramitapp.utilities.AppSettings

abstract class BaseRepository<T : IBaseLocalModel> {

    protected val db: AppDatabase by lazy {
        AppDatabase.getInstance()
    }

    open class BaseRepository<T> {

        protected val db: AppDatabase
            get() = AppDatabase.getInstance()

        protected val localDatabase: String
            get() = "${AppSettings.pathDatabase}/${AppSettings.databaseName}"

        companion object {
            fun init(context: Context) {
                AppDatabase.init(context)
            }
        }
    }
}