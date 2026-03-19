package com.example.stramitapp.ui.Locate_Rfid

import android.graphics.RectF
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stramitapp.utilities.AppSettings
import com.example.stramitapp.zebraconnection.RFIDHandler
import com.zebra.rfid.api3.HANDHELD_TRIGGER_EVENT_TYPE
import com.zebra.rfid.api3.InvalidUsageException
import com.zebra.rfid.api3.OperationFailureException
import com.zebra.rfid.api3.RfidEventsListener
import com.zebra.rfid.api3.RfidReadEvents
import com.zebra.rfid.api3.RfidStatusEvents
import com.zebra.rfid.api3.STATUS_EVENT_TYPE
import com.zebra.rfid.api3.TagData

class LocateTagRFIDViewModel : ViewModel(), RfidEventsListener {

    private val TAG = "LocateTagRFIDVM"
    val tagPattern       = MutableLiveData("")
    val titleText        = MutableLiveData("")
    val relativeDistance = MutableLiveData("0")
    val distanceBox      = MutableLiveData(RectF(0f, 0.05f, 50f, 0f))
    val readerConnection = MutableLiveData("")
    val hintAvailable    = MutableLiveData(false)
    private var disposed = false

    private val isConnected: Boolean
        get() = try {
            RFIDHandler.mConnectedRfidReader?.isConnected == true
        } catch (e: Exception) { false }

    fun updateIn() {
        Log.d(TAG, "updateIn: initializing RFID listener")
        disposed = false
        hintAvailable.postValue(true)

        RFIDHandler.isLocateMode = true

        if (!AppSettings.isRFIDReaderConnection) {
            Log.w(TAG, "updateIn: AppSettings.isRFIDReaderConnection is false")
            readerConnection.postValue("Not connected")
            return
        }

        if (isConnected) {
            try {
                val reader = RFIDHandler.mConnectedRfidReader ?: return
                Log.d(TAG, "updateIn: Reader connected, adding events listener")

                reader.Events.addEventsListener(this)
                reader.Events.setHandheldEvent(true)
                reader.Events.setTagReadEvent(true)
                reader.Events.setInventoryStartEvent(true)
                reader.Events.setInventoryStopEvent(true)

            } catch (e: Exception) {
                Log.e(TAG, "updateIn error: ${e.message}")
                e.printStackTrace()
            }
            updateHints(connected = true)
        } else {
            Log.w(TAG, "updateIn: Reader NOT connected")
            readerConnection.postValue("Not connected")
        }
    }

    fun updateOut() {
        Log.d(TAG, "updateOut: cleaning up")
        hintAvailable.postValue(false)

        RFIDHandler.isLocateMode = false

        try {
            RFIDHandler.mConnectedRfidReader?.Actions?.TagLocationing?.Stop()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            RFIDHandler.mConnectedRfidReader?.Events?.removeEventsListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    override fun eventReadNotify(e: RfidReadEvents?) {
        Log.d(TAG, "eventReadNotify triggered")
        val reader = RFIDHandler.mConnectedRfidReader ?: return

        val tags: Array<TagData>? = try {
            reader.Actions.getReadTags(100)
        } catch (ex: Exception) {
            Log.e(TAG, "Error getting read tags: ${ex.message}")
            ex.printStackTrace()
            null
        }

        if (tags != null) {
            Log.d(TAG, "Tags read: ${tags.size}")
            for (tag in tags) {
                if (tag.isContainsLocationInfo) {
                    val relDist = tag.LocationInfo.getRelativeDistance()
                    Log.d(TAG, "LocationInfo found: relativeDistance=$relDist")
                    updateFillView(relDist)
                    return
                }
            }
        } else {
            Log.d(TAG, "No tags found in eventReadNotify")
        }
    }

    @Synchronized
    override fun eventStatusNotify(e: RfidStatusEvents?) {
        val statusData = e?.StatusEventData ?: return
        Log.d(TAG, "eventStatusNotify: ${statusData.getStatusEventType()}")

        when (statusData.getStatusEventType()) {
            STATUS_EVENT_TYPE.HANDHELD_TRIGGER_EVENT -> {
                val handheldEvent = statusData.HandheldTriggerEventData?.getHandheldEvent()
                val pressed = handheldEvent == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED
                Log.d(TAG, "Trigger event: pressed=$pressed")
                hhTriggerEvent(pressed)
            }
            STATUS_EVENT_TYPE.DISCONNECTION_EVENT -> {
                Log.w(TAG, "Disconnection event received")
                updateHints(connected = false)
            }
            else -> Unit
        }
    }

    @Synchronized
    fun hhTriggerEvent(pressed: Boolean) {
        try {
            val pattern = tagPattern.value
            Log.d(TAG, "hhTriggerEvent: pressed=$pressed, pattern=$pattern")
            if (!pattern.isNullOrEmpty()) {
                locate(start = pressed, pattern = pattern, mask = null)
            } else {
                Log.w(TAG, "hhTriggerEvent: pattern is null or empty")
            }

            readerConnection.postValue(
                when {
                    pressed     -> "Scanning Tags"
                    isConnected -> "Connected"
                    else        -> "Not connected"
                }
            )
        } catch (ex: Exception) {
            Log.e(TAG, "Error in hhTriggerEvent: ${ex.message}")
            ex.printStackTrace()
        }
    }

    @Synchronized
    private fun locate(start: Boolean, pattern: String, mask: String?) {
        Log.d(TAG, "locate: start=$start, pattern=$pattern")
        try {
            val reader = RFIDHandler.mConnectedRfidReader ?: return
            if (start) {
                reader.Actions.TagLocationing.Perform(pattern, mask, null)
                Log.d(TAG, "TagLocationing.Perform called")
            } else {
                reader.Actions.TagLocationing.Stop()
                Log.d(TAG, "TagLocationing.Stop called")
            }
        } catch (e: InvalidUsageException) {
            Log.e(TAG, "locate InvalidUsageException: ${e.message}")
            e.printStackTrace()
        } catch (e: OperationFailureException) {
            Log.e(TAG, "locate OperationFailureException: ${e.results}")
            e.printStackTrace()
        }
    }

    fun updateFillView(relDist: Short) {
        val clamped = relDist.toInt().coerceIn(0, 100)
        val ratio   = clamped / 100f   // 0.0–1.0, maps to full bar height

        Log.d(TAG, "updateFillView: distance=$clamped, ratio=$ratio")
        distanceBox.postValue(RectF(0f, 0.05f, 50f, ratio))
        relativeDistance.postValue(clamped.toString())
    }

    fun updateHints(connected: Boolean = isConnected) {
        readerConnection.postValue(if (connected) "Connected" else "Not connected")
        hintAvailable.postValue(true)
    }

    fun onTagPatternCleared() {
        relativeDistance.value = "0"
        distanceBox.value      = RectF(0f, 0.05f, 50f, 0f)
        updateOut()
    }

    fun dispose() {
        if (disposed) return
        updateOut()
        disposed = true
    }

    override fun onCleared() {
        super.onCleared()
        dispose()
    }
}