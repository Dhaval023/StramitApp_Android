package com.example.stramitapp.ui.movement

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.stramitapp.Global
import com.example.stramitapp.MainActivity
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentMovementBinding
import com.example.stramitapp.zebraconnection.Inventory.TagDataViewModel
import com.example.stramitapp.zebraconnection.RFIDHandler

class MovementFragment : Fragment() {

    private var _binding: FragmentMovementBinding? = null
    private val binding get() = _binding!!

    private var rfidHandler: RFIDHandler? = null
    private lateinit var tagDataViewModel: TagDataViewModel

    private val movementViewModel: MovementViewModel by viewModels {
        MovementViewModelFactory()
    }

    private var scannedListAdapter: ScannedListAdapter? = null

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
        setHasOptionsMenu(true)

        val locationName = arguments?.getString("locationName")
        if (!locationName.isNullOrEmpty()) {
            (requireActivity() as? androidx.appcompat.app.AppCompatActivity)
                ?.supportActionBar?.title = locationName
        }

        val mainActivity = requireActivity() as? MainActivity
        rfidHandler = mainActivity?.getRfidHandler()
        tagDataViewModel = ViewModelProvider(requireActivity()).get(TagDataViewModel::class.java)

        scannedListAdapter = ScannedListAdapter(mutableListOf()) { position ->
            showDeleteSingleConfirmation(position)
        }
        binding.scannedList.adapter = scannedListAdapter

        binding.deleteAll.setOnClickListener {
            showDeleteAllConfirmation()
        }

        movementViewModel.scannedAssets.observe(viewLifecycleOwner) { assets ->
            scannedListAdapter?.updateData(assets)
            updateItemCount(assets.size)
        }

        movementViewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        if (Global.isRfidSelected) {
            setupRfidMode()
        } else {
            setupBarcodeMode()
        }

        updateReaderStatusUI()
        observeConnectionStatus()
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_movement, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
   override  fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_submit -> {
                onSubmitClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onSubmitClicked() {
        val assets = movementViewModel.scannedAssets.value
        if (assets.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No items to submit", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Submit Movement")
            .setMessage("Submit ${assets.size} item(s)?")
            .setPositiveButton("Submit") { _, _ ->
                movementViewModel.submitMovement()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setupBarcodeMode() {
        Log.d("MovementFragment", "Barcode Mode Setup — using hidden bentry EditText")

        val bentry = binding.bentry

        bentry.isFocusable = true
        bentry.isFocusableInTouchMode = true
        bentry.requestFocus()

        bentry.setShowSoftInputOnFocus(false)

        binding.root.setOnClickListener { bentry.requestFocus() }
        binding.scannedList.setOnTouchListener { _, _ ->
            bentry.requestFocus()
            false
        }

        bentry.setOnEditorActionListener { _, actionId, event ->
            val isEnterKey = event?.keyCode == KeyEvent.KEYCODE_ENTER
                    && event.action == KeyEvent.ACTION_DOWN
            val isImeAction = actionId == EditorInfo.IME_ACTION_DONE
                    || actionId == EditorInfo.IME_ACTION_NEXT
                    || actionId == EditorInfo.IME_NULL

            if (isEnterKey || isImeAction) {
                val scanned = bentry.text.toString().trim()
                Log.d("MovementFragment", "bentry EditorAction — scanned: '$scanned'")
                if (scanned.isNotEmpty()) {
                    movementViewModel.onBarcodeScanned(scanned)
                    bentry.setText("")
                }
                true
            } else false
        }

        bentry.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN &&
                (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_TAB)
            ) {
                val scanned = bentry.text.toString().trim()
                Log.d("MovementFragment", "bentry KeyListener — scanned: '$scanned'")
                if (scanned.isNotEmpty()) {
                    movementViewModel.onBarcodeScanned(scanned)
                    bentry.setText("")
                }
                true
            } else false
        }

        bentry.addTextChangedListener(object : android.text.TextWatcher {
            private var lastChangeTime = 0L

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                lastChangeTime = System.currentTimeMillis()
            }

            override fun afterTextChanged(s: android.text.Editable?) {
                val text = s?.toString()?.trim() ?: return
                if (text.isEmpty()) return

                val capturedTime = lastChangeTime
                binding.root.postDelayed({
                    if (_binding == null) return@postDelayed
                    if (lastChangeTime == capturedTime) {
                        val current = bentry.text.toString().trim()
                        if (current.isNotEmpty()) {
                            Log.d("MovementFragment", "bentry TextWatcher — scanned: '$current'")
                            movementViewModel.onBarcodeScanned(current)
                            bentry.setText("")
                        }
                    }
                }, 300)
            }
        })

        Log.d("MovementFragment", "bentry setup complete, focus requested")
    }

    // ── RFID mode ─────────────────────────────────────────────────────────────

    private fun setupRfidMode() {
        Log.d("MovementFragment", "RFID Mode Active")

        rfidHandler?.triggerPressedLiveData?.observe(viewLifecycleOwner) { isPressed ->
            if (isPressed) rfidHandler?.performInventory()
            else rfidHandler?.stopInventory()
        }

        tagDataViewModel.getInventoryItem().observe(viewLifecycleOwner) { tags ->
            tags?.forEach { tagData ->
                val tagId = tagData.tagID
                if (!tagId.isNullOrBlank()) {
                    movementViewModel.onTagScanned(tagId)
                }
            }
        }
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    override fun onResume() {
        super.onResume()
        updateReaderStatusUI()

        if (!Global.isRfidSelected) {
            // Always re-focus bentry when fragment resumes
            binding.bentry.post {
                binding.bentry.requestFocus()
                Log.d("MovementFragment", "onResume — bentry focus requested")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        rfidHandler?.stopInventory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ── UI helpers ────────────────────────────────────────────────────────────

    private fun updateReaderStatusUI() {
        if (Global.isRfidSelected) {
            val isConnected = rfidHandler?.connectionStatus?.value ?: false
            setReaderStatusUI(isConnected)
        } else {
            binding.readerStatus.text = "Barcode Mode Active"
            binding.readerStatus.setTextColor(
                resources.getColor(android.R.color.holo_green_dark, null)
            )
        }
    }

    private fun observeConnectionStatus() {
        if (!Global.isRfidSelected) return
        val handler = rfidHandler ?: run {
            binding.readerStatus.text = "Reader Not Initialized"
            binding.readerStatus.setTextColor(
                resources.getColor(android.R.color.holo_red_dark, null)
            )
            return
        }
        handler.connectionStatus.observe(viewLifecycleOwner) { isConnected ->
            setReaderStatusUI(isConnected)
        }
    }

    private fun setReaderStatusUI(isConnected: Boolean) {
        binding.readerStatus.text = if (isConnected) "Connected" else "Not Connected"
    }

    private fun updateItemCount(count: Int) {
        binding.itemCount.text = if (count == 1) "1 item" else "$count items"
    }

    // ── Dialogs ───────────────────────────────────────────────────────────────

    private fun showDeleteAllConfirmation() {
        val count = movementViewModel.getCount()
        if (count == 0) {
            Toast.makeText(requireContext(), "No items to delete", Toast.LENGTH_SHORT).show()
            return
        }
        AlertDialog.Builder(requireContext())
            .setTitle("Delete All Items")
            .setMessage("Are you sure you want to delete all $count items?")
            .setPositiveButton("Delete") { _, _ -> movementViewModel.clearAll() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteSingleConfirmation(position: Int) {
        val asset = movementViewModel.scannedAssets.value?.getOrNull(position) ?: return
        val displayName = asset.title ?: asset.barcode ?: asset.tag ?: "Unknown"
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Item")
            .setMessage("Delete this item?\n\n$displayName")
            .setPositiveButton("Delete") { _, _ -> movementViewModel.deleteItemAt(position) }
            .setNegativeButton("Cancel", null)
            .show()
    }
}