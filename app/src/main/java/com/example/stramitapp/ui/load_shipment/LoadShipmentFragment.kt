package com.example.stramitapp.ui.load_shipment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentLoadShipmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoadShipmentFragment : Fragment() {

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

        // ✅ UI → ViewModel
        binding.shipmentIdEdittext.doAfterTextChanged { editable ->
            viewModel.onShipmentNumberChanged(editable?.toString().orEmpty())
        }

        // ✅ ViewModel → UI (IMPORTANT FIX)
        viewModel.shipmentNumber.observe(viewLifecycleOwner) { value ->
            if (binding.shipmentIdEdittext.text.toString() != value) {
                binding.shipmentIdEdittext.setText(value)
                binding.shipmentIdEdittext.setSelection(value.length)
            }
        }

        // Button click
        binding.nextButton.setOnClickListener {
            viewModel.onNextClicked()
        }

        // ✅ UI Events
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Barcode from Activity
    fun onBarcodeScanned(barcode: String) {
        viewModel.onBarcodeScanned(barcode)
    }
}