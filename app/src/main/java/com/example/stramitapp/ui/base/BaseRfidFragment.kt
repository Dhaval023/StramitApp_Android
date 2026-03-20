package com.example.stramitapp.ui.base

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stramitapp.Global
import com.example.stramitapp.MainActivity
import com.example.stramitapp.zebraconnection.Inventory.TagDataViewModel
import com.example.stramitapp.zebraconnection.RFIDHandler
import com.zebra.rfid.api3.TagData

abstract class BaseRfidFragment : Fragment() {

    protected var rfidHandler: RFIDHandler? = null
    protected lateinit var tagDataViewModel: TagDataViewModel

    protected fun initRfid() {
        val mainActivity = requireActivity() as? MainActivity
        rfidHandler = mainActivity?.getRfidHandler()
        tagDataViewModel = ViewModelProvider(requireActivity()).get(TagDataViewModel::class.java)
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────

    override fun onResume() {
        super.onResume()
        if (Global.isRfidSelected) {
            rfidHandler?.enableRfidMode()
            tagDataViewModel.getInventoryItem().observe(viewLifecycleOwner) { tags ->
                tags ?: return@observe
                onRfidTagsReceived(tags)
                tags.forEach { tagData ->
                    val tagId = tagData.tagID
                    if (!tagId.isNullOrBlank()) {
                        Log.d("BaseRfidFragment", "RFID Tag: $tagId")
                        onRfidTagScanned(tagId)
                    }
                }
            }
        } else {
            rfidHandler?.enableBarcodeMode()
            onBarcodeReady()
        }
    }

    override fun onPause() {
        super.onPause()
        rfidHandler?.stopInventory()
    }

    // ── Hooks for subclasses ──────────────────────────────────────────────

    /** Called for each individual tag ID scanned. Override in MovementFragment etc. */
    open fun onRfidTagScanned(tagId: String) {}

    /** Called with full TagData array. Override in FloorSweepFragment etc. */
    open fun onRfidTagsReceived(tags: Array<TagData>) {}

    /** Called when barcode mode is active. Override to request focus on bentry. */
    open fun onBarcodeReady() {}
}