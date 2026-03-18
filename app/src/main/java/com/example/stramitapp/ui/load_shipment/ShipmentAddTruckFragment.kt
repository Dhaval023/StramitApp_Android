package com.example.stramitapp.ui.load_shipment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentShipmentAddTruckBinding
import com.example.stramitapp.model.Asset
import com.example.stramitapp.model.CompanyLocation

class ShipmentAddTruckFragment : Fragment() {

    private var _binding: FragmentShipmentAddTruckBinding? = null
    private val binding get() = _binding!!
    private var shipmentNumber: String = ""
    private val viewModel: ShipmentAddTruckViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val items = arguments?.getParcelableArrayList<Asset>("scannedItems") ?: arrayListOf()
        val shipmentNumber = arguments?.getString("shipmentNumber").orEmpty()
        viewModel.scannedItems = items
        viewModel.shipmentNumber = shipmentNumber
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShipmentAddTruckBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shipmentNumber = arguments?.getString("shipmentNumber").orEmpty()
        if (shipmentNumber.isNotEmpty())
            (requireActivity() as? androidx.appcompat.app.AppCompatActivity)
                ?.supportActionBar?.title = shipmentNumber
        setupTruckNumberEntry()
        setupSubmitButton()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupTruckNumberEntry() {
        binding.etTruckNumber.apply {
            inputType = android.text.InputType.TYPE_NULL
            isFocusable = true
            isFocusableInTouchMode = true
            setOnClickListener { viewModel.onTruckNumberTapped() }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) viewModel.onTruckNumberTapped()
            }
        }
    }

    private fun setupSubmitButton() {
        binding.btnSubmit.setOnClickListener {
            viewModel.submitShipmentTruckEvent()
        }
    }

    private fun observeViewModel() {

        viewModel.truckNumberText.observe(viewLifecycleOwner) { name ->
            if (binding.etTruckNumber.text.toString() != name) {
                binding.etTruckNumber.setText(name)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.loaderLayout.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.uiEvent.observe(viewLifecycleOwner) { event ->
            event ?: return@observe

            when {
                event == "SHOW_DROPDOWN" -> {
                    val locations = viewModel.locationList.value ?: emptyList()
                    showLocationDropdown(locations)
                    viewModel.onUiEventConsumed()
                }

                event.startsWith("SUCCESS:") -> {
                    val msg = event.removePrefix("SUCCESS:")
                    viewModel.onUiEventConsumed()
                    AlertDialog.Builder(requireContext())
                        .setTitle("SUCCESS!")
                        .setMessage(msg)
                        .setPositiveButton("OK") { _, _ ->
                            // Navigate via NavController so ShipmentItemResultFragment
                            // is on the NavController back stack — this makes
                            // popBackStack(R.id.nav_home) work from that screen
                            findNavController().navigate(
                                R.id.nav_shipment_item_result,
                                Bundle().apply {
                                    putString("shipment_number", viewModel.shipmentNumber)
                                }
                            )
                        }
                        .setCancelable(false)
                        .show()
                }

                event.startsWith("ERROR:") -> {
                    val msg = event.removePrefix("ERROR:")
                    viewModel.onUiEventConsumed()
                    AlertDialog.Builder(requireContext())
                        .setTitle("ERROR!")
                        .setMessage(msg)
                        .setPositiveButton("OK", null)
                        .show()
                }

                event.startsWith("FAILED:") -> {
                    val msg = event.removePrefix("FAILED:")
                    viewModel.onUiEventConsumed()
                    AlertDialog.Builder(requireContext())
                        .setTitle("FAILED!")
                        .setMessage(msg)
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
        }
    }

    private fun showLocationDropdown(locations: List<CompanyLocation>) {
        if (locations.isEmpty()) {
            AlertDialog.Builder(requireContext())
                .setTitle("Location")
                .setMessage("No locations available.")
                .setPositiveButton("OK", null)
                .show()
            return
        }

        val displayNames = locations.map { it.locationName ?: "Unknown" }.toTypedArray()

        AlertDialog.Builder(requireContext())
            .setTitle("Location")
            .setItems(displayNames) { _, index ->
                viewModel.onLocationSelected(locations[index])
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}