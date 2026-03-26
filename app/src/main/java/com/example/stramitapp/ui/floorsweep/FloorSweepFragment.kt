package com.example.stramitapp.ui.floorsweep

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stramitapp.Global
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentFloorSweepBinding
import com.example.stramitapp.ui.base.BaseRfidFragment
import com.google.gson.Gson
import com.zebra.rfid.api3.TagData
import java.text.SimpleDateFormat
import java.util.*
import androidx.fragment.app.viewModels

class FloorSweepFragment : BaseRfidFragment() {

    private var _binding: FragmentFloorSweepBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FloorSweepViewModel by viewModels()
    private lateinit var scanAdapter: FloorSweepScanAdapter
    private val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFloorSweepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RFID/Barcode via base class
        initRfid()

        setupRecyclerView()
        setupMenu()
        setupObservers()
        setupListeners()

        // Only set up EditText listeners for barcode mode here
        if (!Global.isRfidSelected) setupBarcodeScanner()

        updateReaderStatusUI()
        observeConnectionStatus()
    }

    // ── BaseRfidFragment hooks ────────────────────────────────────────────────

    override fun onRfidTagsReceived(tags: Array<TagData>) {
        Log.d("FloorSweepFragment", "RFID Tags received: ${tags.size}")
        viewModel.onTagRead(tags)
    }

    override fun onBarcodeReady() {
        binding.bentry.post { binding.bentry.requestFocus() }
    }

    override fun onResume() {
        super.onResume() // base handles enableRfidMode/enableBarcodeMode
        viewModel.resetAll()
        updateReaderStatusUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ── RecyclerView ──────────────────────────────────────────────────────────

    private fun setupRecyclerView() {
        scanAdapter = FloorSweepScanAdapter { itemToDelete ->
            viewModel.removeItem(itemToDelete)
        }
        binding.scannedItemsRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = scanAdapter
        }
    }

    // ── Menu ──────────────────────────────────────────────────────────────────

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.floor_sweep_menu, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_submit -> { viewModel.submitEvent(); true }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    // ── Observers ─────────────────────────────────────────────────────────────

    private fun setupObservers() {
        viewModel.floorSweepDateEntry.observe(viewLifecycleOwner) { date ->
            binding.dateEdittext.setText(sdf.format(date))
        }

        viewModel.isDatePickerEnable.observe(viewLifecycleOwner) { enabled ->
            binding.dateEdittext.isEnabled = enabled
            binding.saveButton.isEnabled = enabled
        }

        viewModel.scannedItemsList.observe(viewLifecycleOwner) { list ->
            val size = list.size
            binding.itemCountTextview.text = "$size item${if (size != 1) "s" else ""}"
            scanAdapter.submitList(list)
            if (list.isEmpty()) {
                binding.emptyStateTextview.visibility = View.VISIBLE
                binding.scannedItemsRecyclerview.visibility = View.GONE
            } else {
                binding.emptyStateTextview.visibility = View.GONE
                binding.scannedItemsRecyclerview.visibility = View.VISIBLE
                binding.scannedItemsRecyclerview.scrollToPosition(size - 1)
            }
        }

        viewModel.isBusy.observe(viewLifecycleOwner) { busy ->
            binding.syncLoaderOverlay.visibility = if (busy) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                viewModel.clearErrorMessage()
            }
        }

        viewModel.navigationToResult.observe(viewLifecycleOwner) { resultList ->
            resultList ?: return@observe
            val currentDest = findNavController().currentDestination?.id
            if (currentDest != R.id.nav_floor_sweep) {
                Log.w("FloorSweepFragment", "Navigation skipped — not on floor sweep dest")
                return@observe
            }
            val bundle = Bundle().apply {
                putString("results_json", Gson().toJson(resultList))
            }
            try {
                findNavController().navigate(
                    R.id.action_nav_floor_sweep_to_nav_floor_sweep_result,
                    bundle
                )
            } catch (e: Exception) {
                Log.e("FloorSweepFragment", "Navigation failed: ${e.message}")
                Toast.makeText(context, "Navigation error: ${e.message}", Toast.LENGTH_LONG).show()
            }
            viewModel.onNavigationDone()
        }
    }

    // ── Listeners ─────────────────────────────────────────────────────────────

    private fun setupListeners() {
        binding.dateEdittext.setOnClickListener {
            if (viewModel.isDatePickerEnable.value == true) showDatePicker()
        }
        binding.saveButton.setOnClickListener { showSaveConfirmation() }
        binding.deleteIcon.setOnClickListener { showClearAllConfirmation() }
    }

    // ── Barcode scanner EditText setup ────────────────────────────────────────

    private fun setupBarcodeScanner() {
        Log.d("FloorSweepFragment", "Barcode Mode Setup")
        val bentry = binding.bentry
        bentry.isFocusable = true
        bentry.isFocusableInTouchMode = true
        bentry.requestFocus()
        bentry.setShowSoftInputOnFocus(false)
        binding.root.setOnClickListener { bentry.requestFocus() }

        bentry.setOnEditorActionListener { _, actionId, event ->
            val isEnterKey = event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN
            val isImeAction = actionId == EditorInfo.IME_ACTION_DONE
                    || actionId == EditorInfo.IME_ACTION_NEXT
                    || actionId == EditorInfo.IME_NULL
            if (isEnterKey || isImeAction) {
                val scanned = bentry.text.toString().trim()
                if (scanned.isNotEmpty()) { viewModel.onBarcodeScanned(scanned); bentry.setText("") }
                true
            } else false
        }

        bentry.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN &&
                (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_TAB)) {
                val scanned = bentry.text.toString().trim()
                if (scanned.isNotEmpty()) { viewModel.onBarcodeScanned(scanned); bentry.setText("") }
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
                        if (current.isNotEmpty()) { viewModel.onBarcodeScanned(current); bentry.setText("") }
                    }
                }, 300)
            }
        })
    }

    // ── Reader status ─────────────────────────────────────────────────────────

    private fun updateReaderStatusUI() {
        if (Global.isRfidSelected) {
            val isConnected = rfidHandler?.connectionStatus?.value ?: false
            setReaderStatusUI(isConnected)
        } else {
            binding.readerStatus.text = "Barcode" //Barcode Mode Active
            binding.readerStatus.setTextColor(resources.getColor(android.R.color.holo_green_dark, null))
        }
    }

    private fun observeConnectionStatus() {
        if (!Global.isRfidSelected) return
        val handler = rfidHandler ?: run {
            binding.readerStatus.text = "Reader Not Initialized"
            binding.readerStatus.setTextColor(resources.getColor(android.R.color.holo_red_dark, null))
            return
        }
        handler.connectionStatus.observe(viewLifecycleOwner) { isConnected ->
            setReaderStatusUI(isConnected)
        }
    }

    private fun setReaderStatusUI(isConnected: Boolean) {
        binding.readerStatus.text = if (isConnected) "Connected" else "Not Connected"
    }

    // ── Dialogs ───────────────────────────────────────────────────────────────

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        viewModel.floorSweepDateEntry.value?.let { calendar.time = it }
        DatePickerDialog(requireContext(),
            { _, year, month, dayOfMonth ->
                val c = Calendar.getInstance()
                c.set(year, month, dayOfMonth)
                viewModel.setFloorSweepDate(c.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showSaveConfirmation() {
        val dateStr = sdf.format(viewModel.floorSweepDateEntry.value ?: Date())
        AlertDialog.Builder(requireContext())
            .setTitle("SAVE DATE")
            .setMessage("Are you sure you want to Select Date >>> \"$dateStr\" <<< ?")
            .setPositiveButton("Yes") { _, _ -> viewModel.saveDate() }
            .setNegativeButton("No", null).show()
    }

    private fun showClearAllConfirmation() {
        val count = viewModel.scannedItemsList.value?.size ?: 0
        if (count == 0) return
        AlertDialog.Builder(requireContext())
            .setTitle("CLEAR ALL")
            .setMessage("Are you sure you want to clear all Scanned Items?")
            .setPositiveButton("Yes") { _, _ -> viewModel.clearAll() }
            .setNegativeButton("No", null).show()
    }
}