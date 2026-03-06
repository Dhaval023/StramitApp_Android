package com.example.stramitapp.utilities

import android.bluetooth.BluetoothAdapter

object BluetoothChecker {

    private val adapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    fun isEnabled(): Boolean = adapter?.isEnabled == true

    fun setStatus(enable: Boolean) {
        adapter?.let {
            if (enable && !it.isEnabled) it.enable()
            if (!enable && it.isEnabled) it.disable()
        }
    }
}