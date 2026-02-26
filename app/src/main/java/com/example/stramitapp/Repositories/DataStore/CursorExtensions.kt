package com.example.stramitapp.Repositories.DataStore//package com.example.stramitapp.Repositories.DataStore
//
//import android.content.ContentValues
//import android.database.Cursor
//
//fun Cursor.toApplication(): Application {
//    return Application(
//        appId = getInt(getColumnIndexOrThrow("AppId"))
//        // map remaining fields here
//    )
//}
//
//fun Application.toContentValues(): ContentValues {
//    return ContentValues().apply {
//        put("AppId", appId)
//        // map remaining fields here
//    }
//}