package com.example.stramitapp.ui.search_shipment

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.stramitapp.MainActivity
import com.example.stramitapp.databinding.FragmentSearchShipmentBinding
import com.example.stramitapp.ui.base.BaseRfidFragment
import kotlinx.coroutines.launch

class SearchShipmentFragment : BaseRfidFragment() {

    private var _binding: FragmentSearchShipmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchShipmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[SearchShipmentViewModel::class.java]
        _binding = FragmentSearchShipmentBinding.inflate(inflater, container, false)
        setupSearchResults()
        setupButtons()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRfid()
        setupBarcodeMode()

        rfidHandler?.triggerPressedLiveData?.observe(viewLifecycleOwner) { pressed ->
            if (pressed == true) {
                clearIdField()
            }
        }

        (requireActivity() as? MainActivity)?.getBarcodeHandler()?.getBarcodeDataLiveData()?.observe(viewLifecycleOwner) { data: String? ->
            if (!data.isNullOrEmpty()) {
                val focused = binding.root.findFocus()
                val target = when (focused) {
                    binding.m3CoEdittext -> binding.m3CoEdittext
                    binding.m3DoEdittext -> binding.m3DoEdittext
                    else -> binding.shipmentIdEdittext
                }
                target.setText(data)
                target.setSelection(data.length)
            }
        }
    }

    private fun setupBarcodeMode() {
        binding.shipmentIdEdittext.setShowSoftInputOnFocus(false)
        binding.m3CoEdittext.setShowSoftInputOnFocus(false)
        binding.m3DoEdittext.setShowSoftInputOnFocus(false)

        val keyListener = View.OnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && (keyCode in 280..290 || keyCode == 102 || keyCode == 103)) {
                clearIdField()
            }
            false
        }

        binding.shipmentIdEdittext.setOnKeyListener(keyListener)
        binding.m3CoEdittext.setOnKeyListener(keyListener)
        binding.m3DoEdittext.setOnKeyListener(keyListener)
    }

    override fun onRfidTagScanned(tagId: String) {
        requireActivity().runOnUiThread {
            val focused = binding.root.findFocus()
            val target = when (focused) {
                binding.m3CoEdittext -> binding.m3CoEdittext
                binding.m3DoEdittext -> binding.m3DoEdittext
                else -> binding.shipmentIdEdittext
            }
            target.setText(tagId)
        }
    }

    override fun onBarcodeReady() {
        binding.shipmentIdEdittext.post {
            binding.shipmentIdEdittext.requestFocus()
        }
    }

    fun clearIdField() {
        requireActivity().runOnUiThread {
            val focused = binding.root.findFocus()
            when (focused) {
                binding.m3CoEdittext -> binding.m3CoEdittext.setText("")
                binding.m3DoEdittext -> binding.m3DoEdittext.setText("")
                else -> binding.shipmentIdEdittext.setText("")
            }
        }
    }

    fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && (event.keyCode in 280..290 || event.keyCode == 102 || event.keyCode == 103)) {
            clearIdField()
        }
        return false
    }

    private fun setupSearchResults() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect { isLoading ->
                    binding.searchButton.isEnabled = !isLoading
                    binding.progressBar.visibility =
                        if (isLoading) View.VISIBLE else View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorMessage.collect { message ->
                    if (message.isNotEmpty()) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchResults.collect { results ->
                    if (results.isNotEmpty()) {
                        ShipmentSearchResultFragment
                            .newInstance(ArrayList(results))
                            .show(childFragmentManager, ShipmentSearchResultFragment.TAG)
                    }
                }
            }
        }
    }

    private fun setupButtons() {
        binding.searchButton.setOnClickListener {
            val shipmentNumber = binding.shipmentIdEdittext.text?.toString()?.trim() ?: ""
            val m3CO           = binding.m3CoEdittext.text?.toString()?.trim() ?: ""
            val m3DO           = binding.m3DoEdittext.text?.toString()?.trim() ?: ""

            viewModel.search(
                shipmentNumber = shipmentNumber,
                m3CO           = m3CO,
                m3DO           = m3DO
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}