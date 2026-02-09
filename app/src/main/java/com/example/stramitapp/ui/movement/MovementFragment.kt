package com.example.stramitapp.ui.movement

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stramitapp.Global
import com.example.stramitapp.MainActivity
import com.example.stramitapp.databinding.FragmentMovementBinding
import com.example.stramitapp.zebraconnection.BarcodeHandler
import com.example.stramitapp.zebraconnection.Inventory.TagDataViewModel
import com.example.stramitapp.zebraconnection.RFIDHandler

class MovementFragment : Fragment() {

    private var _binding: FragmentMovementBinding? = null
    private val binding get() = _binding!!

    private var rfidHandler: RFIDHandler? = null
    private var barcodeHandler: BarcodeHandler? = null
    private lateinit var tagDataViewModel: TagDataViewModel
    private var scannedListAdapter: ScannedListAdapter? = null

    private val scannedTagsList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = requireActivity() as? MainActivity
        rfidHandler = mainActivity?.getRfidHandler()
        barcodeHandler = mainActivity?.getBarcodeHandler()
        tagDataViewModel = ViewModelProvider(requireActivity()).get(TagDataViewModel::class.java)

        scannedListAdapter = ScannedListAdapter(mutableListOf()) { position ->
            showDeleteSingleConfirmation(position)
        }
        binding.scannedList.adapter = scannedListAdapter

        binding.deleteAll.setOnClickListener {
            showDeleteAllConfirmation()
        }

        if (Global.isRfidSelected) {
            setupRfidMode()
        } else {
            setupBarcodeMode()
        }

        observeConnectionStatus()
    }

    private fun observeConnectionStatus() {
        rfidHandler?.connectionStatus?.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                binding.readerStatus.text = "Reader Connected"
                binding.readerStatus.setTextColor(resources.getColor(android.R.color.holo_green_dark, null))
            } else {
                binding.readerStatus.text = "Reader Disconnected"
                binding.readerStatus.setTextColor(resources.getColor(android.R.color.holo_red_dark, null))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!Global.isRfidSelected) {
            barcodeHandler?.registerBarcodeReceiver()
            Log.d("MovementFragment", "Barcode receiver registered")
        }
    }

    override fun onPause() {
        super.onPause()
        if (!Global.isRfidSelected) {
            barcodeHandler?.unregisterBarcodeReceiver()
            Log.d("MovementFragment", "Barcode receiver unregistered")
        }
        rfidHandler?.stopInventory()
    }

    private fun setupRfidMode() {
        Log.d("MovementFragment", "RFID Mode Active")

        rfidHandler?.triggerPressedLiveData?.observe(viewLifecycleOwner) { isPressed ->
            if (isPressed) {
                rfidHandler?.performInventory()
            } else {
                rfidHandler?.stopInventory()
            }
        }

        tagDataViewModel.getInventoryItem().observe(viewLifecycleOwner) { items ->
            val tagIds = items.map { it.tagID }

            tagIds.forEach { tagId ->
                if (!scannedTagsList.contains(tagId)) {
                    scannedTagsList.add(tagId)
                }
            }
            scannedListAdapter?.updateData(scannedTagsList)
            updateItemCount()
        }
    }

    private fun setupBarcodeMode() {
        Log.d("MovementFragment", "Barcode Mode Active")

        if (barcodeHandler == null) {
            Log.e("MovementFragment", "BarcodeHandler is NULL!")
            Toast.makeText(requireContext(), "Barcode handler not initialized", Toast.LENGTH_LONG).show()
            return
        }

        barcodeHandler?.getBarcodeDataLiveData()?.observe(viewLifecycleOwner) { barcodeData ->
            if (barcodeData.isNotEmpty()) {
                Log.d("MovementFragment", "Barcode scanned: $barcodeData")

                if (!scannedTagsList.contains(barcodeData)) {
                    scannedTagsList.add(barcodeData)
                    scannedListAdapter?.updateData(scannedTagsList)
                    updateItemCount()
                    Toast.makeText(requireContext(), "Barcode added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Barcode already scanned", Toast.LENGTH_SHORT).show()
                }

                // It's important to clear the live data to ensure new scans are detected
                barcodeHandler?.clearBarcodeData()
            }
        }
    }

    private fun updateItemCount() {
        binding.itemCount.text = if (scannedTagsList.size == 1) {
            "1 item"
        } else {
            "${scannedTagsList.size} items"
        }
    }

    private fun showDeleteAllConfirmation() {
        val itemCount = scannedListAdapter?.getCount() ?: 0
        if (itemCount == 0) {
            Toast.makeText(requireContext(), "No items to delete", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Delete All Items")
            .setMessage("Are you sure you want to delete all $itemCount items?")
            .setPositiveButton("Delete") { _, _ ->
                scannedListAdapter?.clearAll()
                scannedTagsList.clear()
                binding.itemCount.text = "0 items"
                Toast.makeText(requireContext(), "All items deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteSingleConfirmation(position: Int) {
        val tagId = scannedListAdapter?.getItem(position) ?: return

        AlertDialog.Builder(requireContext())
            .setTitle("Delete Item")
            .setMessage("""Delete this item?

$tagId""")
            .setPositiveButton("Delete") { _, _ ->
                scannedListAdapter?.removeItem(position)
                scannedTagsList.removeAt(position)
                updateItemCount()
                Toast.makeText(requireContext(), "Item deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}