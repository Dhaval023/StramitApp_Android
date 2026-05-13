package com.example.stramitapp.ui.addlocationmovement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
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
                        R.layout.item_dropdown,
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

        binding.companyAutocompleteTextview.setOnItemClickListener { _, _, position, _ ->
            val companiesList: List<Company> = viewModel.companies.value
            val company: Company = companiesList[position]
            updateSelectedCompany(company)
        }

        binding.companyAutocompleteTextview.doAfterTextChanged { s ->
            binding.companyTextInputLayout.error = null
            val text = s.toString().trim()
            val match = viewModel.companies.value.find { it.companyName?.trim() == text }
            if (match != null) {
                if (match != selectedCompany) {
                    updateSelectedCompany(match)
                }
            } else {
                selectedCompany = null
                AppSettings.tempSelectedSystem = null
            }
        }

        binding.companyAutocompleteTextview.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                val text = binding.companyAutocompleteTextview.text.toString().trim()
                val match = viewModel.companies.value.find { it.companyName?.trim() == text }
                if (match == null && text.isNotEmpty()) {
                    binding.companyTextInputLayout.error = "Invalid Company"
                } else if (match != null) {
                    binding.companyTextInputLayout.error = null
                    binding.destinationLocationAutocompleteTextview.requestFocus()
                }
                true
            } else false
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.locations.collect { locations: List<CompanyLocation> ->
                    val names = locations.map { l: CompanyLocation -> l.locationName ?: "" }
                    val adapter = ArrayAdapter(
                        requireContext(),
                        R.layout.item_dropdown,
                        names
                    )
                    binding.destinationLocationAutocompleteTextview.setAdapter(adapter)
                }
            }
        }

        binding.destinationLocationAutocompleteTextview.setOnItemClickListener { _, _, position, _ ->
            val locationsList: List<CompanyLocation> = viewModel.locations.value
            val location: CompanyLocation = locationsList[position]
            selectedLocation = location
            AppSettings.tempSelectedLocation = location
            binding.destinationLocationTextInputLayout.error = null
        }

        binding.destinationLocationAutocompleteTextview.doAfterTextChanged { s ->
            binding.destinationLocationTextInputLayout.error = null
            val text = s.toString().trim()
            val match = viewModel.locations.value.find { it.locationName?.trim() == text }
            if (match != null) {
                selectedLocation = match
                AppSettings.tempSelectedLocation = match
            } else {
                selectedLocation = null
                AppSettings.tempSelectedLocation = null
            }
        }

        binding.destinationLocationAutocompleteTextview.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                val text = binding.destinationLocationAutocompleteTextview.text.toString().trim()
                val match = viewModel.locations.value.find { it.locationName?.trim() == text }
                if (match == null && text.isNotEmpty()) {
                    binding.destinationLocationTextInputLayout.error = "Invalid Location"
                } else if (match != null) {
                    binding.destinationLocationTextInputLayout.error = null
                    binding.nextButton.performClick()
                }
                true
            } else false
        }

        binding.nextButton.setOnClickListener {
            val companyText = binding.companyAutocompleteTextview.text.toString().trim()
            val locationText = binding.destinationLocationAutocompleteTextview.text.toString().trim()

            var isValid = true

            if (companyText.isEmpty()) {
                binding.companyTextInputLayout.error = "Please select a Company"
                isValid = false
            } else {
                val companyMatch = viewModel.companies.value.find { it.companyName?.trim() == companyText }
                if (companyMatch == null) {
                    binding.companyTextInputLayout.error = "Invalid Company. Please select from the list."
                    isValid = false
                } else {
                    selectedCompany = companyMatch
                    AppSettings.tempSelectedSystem = companyMatch
                    binding.companyTextInputLayout.error = null
                }
            }

            if (locationText.isEmpty()) {
                binding.destinationLocationTextInputLayout.error = "Please select a Destination Location"
                isValid = false
            } else {
                val locationMatch = viewModel.locations.value.find { it.locationName?.trim() == locationText }
                if (locationMatch == null) {
                    binding.destinationLocationTextInputLayout.error = "Invalid Location. Please select from the list."
                    isValid = false
                } else {
                    selectedLocation = locationMatch
                    AppSettings.tempSelectedLocation = locationMatch
                    binding.destinationLocationTextInputLayout.error = null
                }
            }

            if (isValid) {
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

    private fun updateSelectedCompany(company: Company) {
        selectedCompany = company
        AppSettings.tempSelectedSystem = company

        selectedLocation = null
        binding.destinationLocationAutocompleteTextview.setText("", false)

        viewModel.loadLocationsByCompany(company.companyId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}