package com.example.stramitapp

object Global {
    var isRfidSelected: Boolean = true
        private set

    var isBarcodeSelected: Boolean = false
        private set

    const val databaseName = ""
    const val deviceId = ""

    fun setRfidMode() {
        isRfidSelected = true
        isBarcodeSelected = false
    }

    fun setBarcodeMode() {
        isRfidSelected = false
        isBarcodeSelected = true
    }

    fun getCurrentScanType(): String {
        return if (isRfidSelected) "RFID" else "BARCODE"
    }
}