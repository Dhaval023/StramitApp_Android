package com.example.stramitapp.models.Interfaces.Local

import androidx.databinding.BaseObservable
import com.example.stramitapp.models.Interfaces.Base.IBaseLocalModel

open class BaseLocalModel : BaseObservable(), IBaseLocalModel {

    override val id: Int = 0

    override fun notifyPropertyChanged(propertyId: Int) {
        super.notifyPropertyChanged(propertyId)
    }

    protected fun <T> setProperty(
        field: T,
        value: T,
        propertyId: Int
    ): T {
        if (field == value) return field

        notifyPropertyChanged(propertyId)
        return value
    }
}
