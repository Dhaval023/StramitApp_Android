package com.example.stramitapp.ui.search_asset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentSearchAssetBinding
import com.example.stramitapp.model.Company
import com.example.stramitapp.model.CompanyLocation
import com.example.stramitapp.utilities.AppSettings
import kotlinx.coroutines.launch

class SearchAssetFragment : Fragment() {

    private var _binding: FragmentSearchAssetBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchAssetViewModel
    private var selectedCompany: Company? = null
    private var selectedLocation: CompanyLocation? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[SearchAssetViewModel::class.java]
        _binding = FragmentSearchAssetBinding.inflate(inflater, container, false)
        viewModel.loadCompanies()
        setupCompanyDropdown()
        setupLocationDropdown()
        setupSearchResults()
        setupButtons()
        return binding.root
    }

    private fun setupCompanyDropdown() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.companies.collect { companies: List<Company> ->
                    val names = companies.map { it.companyName ?: "" }
                    val adapter = ArrayAdapter(
                        requireContext(),
                        R.layout.item_dropdown,
                        names
                    )
                    binding.companyAutocompleteTextview.setAdapter(adapter)

                    val itemHeight = 48
                    val maxVisibleItems = 5
                    val visibleItems = minOf(companies.size, maxVisibleItems)
                    val density = resources.displayMetrics.density
                    binding.companyAutocompleteTextview.dropDownHeight =
                        (itemHeight * visibleItems * density).toInt()

                    val saved = AppSettings.tempSelectedSystem
                    if (saved != null) {
                        val match = companies.find { it.companyId == saved.companyId }
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
            val company = viewModel.companies.value[position]
            selectedCompany = company
            AppSettings.tempSelectedSystem = company
            selectedLocation = null
            binding.locationAutocompleteTextview.setText("", false)
            viewModel.loadLocationsByCompany(company.companyId)
        }
    }

    private fun setupLocationDropdown() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.locations.collect { locations: List<CompanyLocation> ->
                    val names = locations.map { it.locationName ?: "" }
                    val adapter = ArrayAdapter(
                        requireContext(),
                        R.layout.item_dropdown,
                        names
                    )
                    binding.locationAutocompleteTextview.setAdapter(adapter)

                    val itemHeight = 48
                    val maxVisibleItems = 5
                    val visibleItems = minOf(locations.size, maxVisibleItems)
                    val density = resources.displayMetrics.density
                    binding.locationAutocompleteTextview.dropDownHeight =
                        (itemHeight * visibleItems * density).toInt()

                    val saved = AppSettings.tempSelectedLocation
                    if (saved != null) {
                        val match = locations.find { it.locationId == saved.locationId }
                        if (match != null) {
                            selectedLocation = match
                            binding.locationAutocompleteTextview.setText(
                                match.locationName ?: "", false
                            )
                        }
                    }
                }
            }
        }

        binding.locationAutocompleteTextview.setOnItemClickListener { _, _, position, _ ->
            val location = viewModel.locations.value[position]
            selectedLocation = location
            AppSettings.tempSelectedLocation = location
        }
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
                    if (!message.isNullOrEmpty()) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchResults.collect { results ->
                    if (results.isNotEmpty()) {
                        SearchResultFragment.newInstance(ArrayList(results))
                            .show(childFragmentManager, SearchResultFragment.TAG)
                    }
                }
            }
        }
    }

    private fun setupButtons() {
        binding.searchButton.setOnClickListener {
            val companyId = selectedCompany?.companyId
            if (companyId == null) {
                Toast.makeText(
                    requireContext(),
                    "Please select a Company before searching.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val locationId = selectedLocation?.locationId ?: 0
            val barcode = binding.idEdittext.text?.toString()?.trim() ?: ""

            viewModel.search(
                companyId = companyId,
                locationId = locationId,
                barcode = barcode
            )
        }

        binding.resetButton.setOnClickListener {
            selectedCompany = null
            selectedLocation = null
            binding.companyAutocompleteTextview.setText("", false)
            binding.locationAutocompleteTextview.setText("", false)
            binding.idEdittext.setText("")
            viewModel.reset()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}