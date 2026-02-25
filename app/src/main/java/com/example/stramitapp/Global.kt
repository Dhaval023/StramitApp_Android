package com.example.stramitapp

object Global {
    var isRfidSelected: Boolean = true
        private set

    var isBarcodeSelected: Boolean = false
        private set

    const val databaseName = ""
    const val deviceId = ""

    // Use these functions to update the values
    fun setRfidMode() {
        isRfidSelected = true
        isBarcodeSelected = false
    }

    fun setBarcodeMode() {
        isRfidSelected = false
        isBarcodeSelected = true
    }

    // Helper method to get current scan type as string
    fun getCurrentScanType(): String {
        return if (isRfidSelected) "RFID" else "BARCODE"
    }
}