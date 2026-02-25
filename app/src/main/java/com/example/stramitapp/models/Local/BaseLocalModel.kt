package com.example.stramitapp.models.Local

import androidx.databinding.BaseObservable
import com.example.stramitapp.models.Interfaces.Base.IBaseLocalModel

open class BaseLocalModel : BaseObservable(), IBaseLocalModel {

    protected fun <T> setProperty(current: T, new: T, onChange: () -> Unit): T {
        return if (current != new) {
            onChange()
            new
        } else {
            current
        }
    }
}