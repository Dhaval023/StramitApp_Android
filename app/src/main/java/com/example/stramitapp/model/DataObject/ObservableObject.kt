package com.example.stramitapp.model.DataObject

import androidx.databinding.BaseObservable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

open class ObservableObject : BaseObservable() {

    protected fun <T> setProperty(
        field: T,
        value: T,
        propertyId: Int,
        onChanged: (() -> Unit)? = null
    ): T {
        if (field == value) return field

        onChanged?.invoke()
        notifyPropertyChanged(propertyId)
        return value
    }

    fun setProperty(value: String?): Date {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(SimpleDateFormat().parse(value ?: "") ?: Date(0))

            inputFormat.parse(date) ?: Date(0)

        } catch (e: Exception) {
            Date(0)
        }
    }
}
