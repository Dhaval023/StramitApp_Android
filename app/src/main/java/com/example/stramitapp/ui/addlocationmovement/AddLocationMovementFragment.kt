package com.example.stramitapp.ui.addlocationmovement

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentAddLocationMovementBinding
import com.example.stramitapp.model.Company
import com.example.stramitapp.model.CompanyLocation
import com.example.stramitapp.utilities.AppSettings
import kotlinx.coroutines.launch

class AddLocationMovementFragment : Fragment() {

    private var _binding: FragmentAddLocationMovementBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AddLocationMovementViewModel

    private var selectedCompany: Company? = null
    private var selectedLocation: CompanyLocation? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[AddLocationMovementViewModel::class.java]
        _binding = FragmentAddLocationMovementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadCompanies()

        // Observe companies
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.companies.collect { companies: List<Company> ->
                    val names = companies.map { c: Company -> c.companyName ?: "" }
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        names
                    )
                    binding.companyAutocompleteTextview.setAdapter(adapter)

                    // Pre-select saved system
                    val saved = AppSettings.tempSelectedSystem
                    if (saved != null) {
                        val match = companies.find { c: Company -> c.companyId == saved.companyId }
                        if (match != null) {
                            selectedCompany = match
                            binding.companyAutocompleteTextview.setText(
                                match.companyName ?: "", false
                            )
                            viewModel.loadLocationsByCompany(match.companyId)
                        }
                    }
                }
            }
        }

        // Company selection
        binding.companyAutocompleteTextview.setOnItemClickListener { _, _, position, _ ->
            val companiesList: List<Company> = viewModel.companies.value
            val company: Company = companiesList[position]
            selectedCompany = company
            AppSettings.tempSelectedSystem = company

            // Reset location
            selectedLocation = null
            binding.destinationLocationAutocompleteTextview.setText("", false)

            viewModel.loadLocationsByCompany(company.companyId)
        }

        // Observe locations
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.locations.collect { locations: List<CompanyLocation> ->
                    val names = locations.map { l: CompanyLocation -> l.locationName ?: "" }
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        names
                    )
                    binding.destinationLocationAutocompleteTextview.setAdapter(adapter)
                }
            }
        }

        // Location selection
        binding.destinationLocationAutocompleteTextview.setOnItemClickListener { _, _, position, _ ->
            val locationsList: List<CompanyLocation> = viewModel.locations.value
            val location: CompanyLocation = locationsList[position]
            selectedLocation = location
            AppSettings.tempSelectedLocation = location
        }

        // Next button
        binding.nextButton.setOnClickListener {
            if (selectedCompany == null || selectedLocation == null) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Info")
                    .setMessage("Please fill up all fields before proceeding.")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
            } else {
                val bundle = Bundle().apply {
                    putString("locationName", selectedLocation?.locationName ?: "")
                }
                findNavController().navigate(
                    R.id.action_nav_movement_to_nav_movement_scanned_items,
                    bundle
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}