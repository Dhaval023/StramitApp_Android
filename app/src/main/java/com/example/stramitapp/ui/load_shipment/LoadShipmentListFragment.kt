package com.example.stramitapp.ui.load_shipment

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.stramitapp.Global
import com.example.stramitapp.MainActivity
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentLoadShipmentListBinding
import com.example.stramitapp.zebraconnection.Inventory.TagDataViewModel
import com.example.stramitapp.zebraconnection.RFIDHandler

class LoadShipmentListFragment : Fragment() {

    private var _binding: FragmentLoadShipmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ShipmentListViewModel
    private var rfidHandler: RFIDHandler? = null
    private lateinit var tagDataViewModel: TagDataViewModel
    private var listAdapter: ScannedAssetAdapter? = null
    private var listView: ListView? = null
    private var shipmentNumber: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadShipmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            handleNext()
        }

        shipmentNumber = arguments?.getString("shipmentNumber").orEmpty()
        if (shipmentNumber.isNotEmpty())
            (requireActivity() as? androidx.appcompat.app.AppCompatActivity)
                ?.supportActionBar?.title = shipmentNumber

        Log.d("LoadShipmentListFragment", "shipmentNumber from bundle: '$shipmentNumber'")
        viewModel = ViewModelProvider(requireActivity())[ShipmentListViewModel::class.java]

        val mainActivity = requireActivity() as? MainActivity
        rfidHandler = mainActivity?.getRfidHandler()
        tagDataViewModel = ViewModelProvider(requireActivity())[TagDataViewModel::class.java]

        listView = view.findViewById(com.example.stramitapp.R.id.recyclerItems)

        listAdapter = ScannedAssetAdapter(
            requireContext(),
            R.layout.item_scanned,
            mutableListOf()
        ) { assetToDelete ->
            viewModel.deleteItem(assetToDelete)
        }
        listView?.adapter = listAdapter

        viewModel.items.observe(viewLifecycleOwner) { items ->
            Log.d("LoadShipmentListFragment", "List updated — size: ${items.size}")
            val displayList = items.map { asset ->
                asset.title ?: asset.barcode ?: "Unknown"
            }.toMutableList()
            listAdapter?.clear()
            listAdapter?.addAll(items)
            listAdapter?.notifyDataSetChanged()
            binding.txtCount.text = "Total: ${items.size}"
        }

        viewModel.uiEvent.observe(viewLifecycleOwner) { message ->
            message ?: return@observe
            AlertDialog.Builder(requireContext())
                .setTitle("ERROR!")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show()
            viewModel.onEventConsumed()
        }

        binding.btnClear.setOnClickListener {
            viewModel.clearAll()
        }

        setupPowerControl()

        if (Global.isRfidSelected) setupRfid()
        else setupBarcode()

        observeReaderConnection()
    }

    private fun setupBarcode() {
        Log.d("LoadShipmentListFragment", "Barcode Mode Setup")

        val bentry = binding.bentry
        bentry.isFocusable = true
        bentry.isFocusableInTouchMode = true
        bentry.requestFocus()
        bentry.setShowSoftInputOnFocus(false)

        binding.root.setOnClickListener { bentry.requestFocus() }
        listView?.setOnTouchListener { _, _ -> bentry.requestFocus(); false }

        bentry.setOnEditorActionListener { _, actionId, event ->
            val isEnterKey = event?.keyCode == KeyEvent.KEYCODE_ENTER
                    && event.action == KeyEvent.ACTION_DOWN
            val isImeAction = actionId == EditorInfo.IME_ACTION_DONE
                    || actionId == EditorInfo.IME_ACTION_NEXT
                    || actionId == EditorInfo.IME_NULL
            if (isEnterKey || isImeAction) {
                val scanned = bentry.text.toString().trim()
                if (scanned.isNotEmpty()) {
                    viewModel.onItemScanned(scanned, shipmentNumber)
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
                if (scanned.isNotEmpty()) {
                    viewModel.onItemScanned(scanned, shipmentNumber)
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
                            viewModel.onItemScanned(current, shipmentNumber)
                            bentry.setText("")
                        }
                    }
                }, 1)
            }
        })
    }
    private fun setupRfid() {
        rfidHandler?.triggerPressedLiveData?.observe(viewLifecycleOwner) { pressed ->
            if (pressed) rfidHandler?.performInventory()
            else rfidHandler?.stopInventory()
        }
        tagDataViewModel.getInventoryItem().observe(viewLifecycleOwner) { tags ->
            tags?.forEach { tag ->
                val tagId = tag.tagID
                if (!tagId.isNullOrBlank()) {
                    viewModel.onItemScanned(tagId, shipmentNumber)
                }
            }
        }
    }

    private fun handleNext() {
        val items = viewModel.items.value ?: emptyList()

        if (items.isEmpty()) {
            AlertDialog.Builder(requireContext())
                .setTitle("ERROR!")
                .setMessage("No scanned items. Please scan and try again.")
                .setPositiveButton(android.R.string.ok, null)
                .show()
            return
        }

        val bundle = Bundle().apply {
            putString("shipmentNumber", shipmentNumber)
            putParcelableArrayList("scannedItems", ArrayList(items))
        }

        findNavController().navigate(
            R.id.action_currentFragment_to_shipmentAddTruckFragment,
            bundle
        )
    }

    // ── Connection Status ─────────────────────────────────────────────────────

    private fun observeReaderConnection() {
        if (!Global.isRfidSelected) {
            binding.readerStatus.text = "Barcode Mode Active"
            binding.readerStatus.setTextColor(
                resources.getColor(android.R.color.holo_green_dark, null)
            )
            return
        }
        rfidHandler?.connectionStatus?.observe(viewLifecycleOwner) { connected ->
            binding.readerStatus.text = if (connected) "Connected" else "Not Connected"
            binding.readerStatus.setTextColor(
                resources.getColor(
                    if (connected) android.R.color.holo_green_dark
                    else android.R.color.holo_red_dark, null
                )
            )
        }
    }

    private fun setupPowerControl() {
        val defaultPower = 270
        binding.seekPower.progress = defaultPower
        binding.txtPowerValue.text = "Power: $defaultPower dBm"
        binding.seekPower.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, value: Int, fromUser: Boolean) {
                binding.txtPowerValue.text = "Power: $value dBm"
                if (fromUser && Global.isRfidSelected) rfidHandler
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        if (!Global.isRfidSelected) {
            binding.bentry.post { binding.bentry.requestFocus() }
        }
    }

    override fun onPause() {
        super.onPause()
        rfidHandler?.stopInventory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listView?.adapter = null
        listView = null
        _binding = null
    }
}