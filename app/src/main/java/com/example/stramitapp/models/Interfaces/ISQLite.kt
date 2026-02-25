package com.astrack.models.interfaces

import android.database.sqlite.SQLiteDatabase

interface ISQLite {
    fun getConnection(): SQLiteDatabase
}