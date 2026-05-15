package com.example.stramitapp.ui.search_asset

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentSearchAssetBinding
import com.example.stramitapp.model.Company
import com.example.stramitapp.model.CompanyLocation
import com.example.stramitapp.ui.base.BaseRfidFragment
import com.example.stramitapp.utilities.AppSettings
import kotlinx.coroutines.launch

class SearchAssetFragment : BaseRfidFragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRfid()
        setupBarcodeMode()
    }

    private fun setupBarcodeMode() {
        val bentry = binding.idEdittext
        bentry.setShowSoftInputOnFocus(false)

        bentry.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onRfidTagScanned(tagId: String) {
        requireActivity().runOnUiThread {
            binding.idEdittext.setText(tagId)
        }
    }

    override fun onBarcodeReady() {
        binding.idEdittext.post {
            binding.idEdittext.requestFocus()
        }
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
            updateSelectedCompany(company)
        }

        binding.companyAutocompleteTextview.doAfterTextChanged { s ->
            binding.companyTextInputLayout.error = null
            val text = s.toString().trim()
            val match = viewModel.companies.value.find { it.companyName?.trim() == text }
            if (match != null && match != selectedCompany) {
                updateSelectedCompany(match)
            } else if (match == null) {
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
                    binding.locationAutocompleteTextview.requestFocus()
                }
                true
            } else false
        }
    }

    private fun updateSelectedCompany(company: Company) {
        selectedCompany = company
        AppSettings.tempSelectedSystem = company
        selectedLocation = null
        binding.locationAutocompleteTextview.setText("", false)
        viewModel.loadLocationsByCompany(company.companyId)
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
            binding.locationTextInputLayout.error = null
        }

        binding.locationAutocompleteTextview.doAfterTextChanged { s ->
            binding.locationTextInputLayout.error = null
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

        binding.locationAutocompleteTextview.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                val text = binding.locationAutocompleteTextview.text.toString().trim()
                val match = viewModel.locations.value.find { it.locationName?.trim() == text }
                if (match == null && text.isNotEmpty()) {
                    binding.locationTextInputLayout.error = "Invalid Location"
                } else if (match != null) {
                    binding.locationTextInputLayout.error = null
                    binding.idEdittext.requestFocus()
                }
                true
            } else false
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
        binding.idEdittext.setOnEditorActionListener { _, actionId, event ->
            val isEnterKey = (event?.keyCode == KeyEvent.KEYCODE_ENTER || event?.keyCode == KeyEvent.KEYCODE_TAB)
                    && event.action == KeyEvent.ACTION_DOWN
            val isImeAction = actionId == EditorInfo.IME_ACTION_DONE
                    || actionId == EditorInfo.IME_ACTION_NEXT
                    || actionId == EditorInfo.IME_NULL
            if (isEnterKey || isImeAction) {
                val current = binding.idEdittext.text.toString().trim()
                performSearch(current)
                true
            } else false
        }

        binding.searchButton.setOnClickListener {
            val current = binding.idEdittext.text.toString().trim()
            performSearch(current)
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

    private fun performSearch(barcode: String) {
        val companyText = binding.companyAutocompleteTextview.text.toString().trim()
        val locationText = binding.locationAutocompleteTextview.text.toString().trim()

        if (companyText.isEmpty() && locationText.isEmpty() && barcode.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill up any field before proceeding.", Toast.LENGTH_SHORT).show()
            return
        }

        var isValid = true
        var companyId = 0
        var locationId = 0

        // Validate Company
        if (companyText.isNotEmpty()) {
            val match = viewModel.companies.value.find { it.companyName?.trim() == companyText }
            if (match == null) {
                binding.companyTextInputLayout.error = "Invalid Company. Please select from the list."
                isValid = false
            } else {
                companyId = match.companyId
                selectedCompany = match
                AppSettings.tempSelectedSystem = match
                binding.companyTextInputLayout.error = null
            }
        }

        // Validate Location
        if (locationText.isNotEmpty()) {
            val match = viewModel.locations.value.find { it.locationName?.trim() == locationText }
            if (match == null) {
                binding.locationTextInputLayout.error = "Invalid Location. Please select from the list."
                isValid = false
            } else {
                locationId = match.locationId
                selectedLocation = match
                AppSettings.tempSelectedLocation = match
                binding.locationTextInputLayout.error = null
            }
        }

        if (!isValid) return

        // Dismiss previous result dialog if it's still showing
        childFragmentManager.findFragmentByTag(SearchResultFragment.TAG)?.let {
            (it as? DialogFragment)?.dismiss()
        }

        viewModel.search(
            companyId = companyId,
            locationId = locationId,
            barcode = barcode
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}