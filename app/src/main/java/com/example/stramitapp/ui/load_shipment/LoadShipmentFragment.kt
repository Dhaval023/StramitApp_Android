package com.example.stramitapp.ui.load_shipment

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.stramitapp.MainActivity
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentLoadShipmentBinding
import com.example.stramitapp.ui.base.BaseRfidFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoadShipmentFragment : BaseRfidFragment() {

    private var _binding: FragmentLoadShipmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoadShipmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadShipmentBinding.inflate(inflater, container, false)
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
                binding.shipmentIdEdittext.setText(data)
                binding.shipmentIdEdittext.setSelection(data.length)
            }
        }

        binding.shipmentIdEdittext.doAfterTextChanged { editable ->
            viewModel.onShipmentNumberChanged(editable?.toString().orEmpty())
        }

        viewModel.shipmentNumber.observe(viewLifecycleOwner) { value ->
            if (binding.shipmentIdEdittext.text.toString() != value) {
                binding.shipmentIdEdittext.setText(value)
                binding.shipmentIdEdittext.setSelection(value.length)
            }
        }

        binding.nextButton.setOnClickListener {
            viewModel.onNextClicked()
        }

        viewModel.uiEvent.observe(viewLifecycleOwner) { event ->
            event ?: return@observe

            when (event) {
                is LoadShipmentViewModel.UiEvent.NavigateToList -> {
                    val bundle = Bundle().apply {
                        putString("shipmentNumber", event.shipmentNumber)
                    }

                    findNavController().navigate(
                        R.id.action_nav_load_shipment_to_nav_load_shipment_list,
                        bundle
                    )

                    viewModel.onEventConsumed()
                }

                is LoadShipmentViewModel.UiEvent.ShowError -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("ERROR!")
                        .setMessage(event.message)
                        .setPositiveButton(android.R.string.ok, null)
                        .show()

                    viewModel.onEventConsumed()
                }
            }
        }
    }

    private fun setupBarcodeMode() {
        val bentry = binding.shipmentIdEdittext
        bentry.setShowSoftInputOnFocus(false)

        bentry.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && (keyCode in 280..290 || keyCode == 102 || keyCode == 103)) {
                clearIdField()
            }
            false
        }
    }

    override fun onRfidTagScanned(tagId: String) {
        requireActivity().runOnUiThread {
            binding.shipmentIdEdittext.setText(tagId)
        }
    }

    override fun onBarcodeReady() {
        binding.shipmentIdEdittext.post {
            binding.shipmentIdEdittext.requestFocus()
        }
    }

    fun clearIdField() {
        requireActivity().runOnUiThread {
            if (binding.shipmentIdEdittext.text?.isNotEmpty() == true) {
                binding.shipmentIdEdittext.setText("")
            }
        }
    }

    fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && (event.keyCode in 280..290 || event.keyCode == 102 || event.keyCode == 103)) {
            clearIdField()
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}