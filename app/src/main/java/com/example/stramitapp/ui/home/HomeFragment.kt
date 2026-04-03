package com.example.stramitapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.stramitapp.Global
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentHomeBinding
import com.example.stramitapp.model.Company
import com.example.stramitapp.services.SyncService
import com.example.stramitapp.ui.login.LoginViewModel
import com.example.stramitapp.utilities.AppSettings
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by activityViewModels()
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.authenticatedUser.collect { user ->
                Log.d("HomeFragment", "user received: $user")
                val firstName = user?.firstName ?: ""
                val lastName  = user?.lastName  ?: ""
                val fullName  = "$firstName $lastName".trim()
                binding.fullName = fullName.ifEmpty { "" }
            }
        }

        if (Global.isRfidSelected) {
            binding.readerModeRadioGroup?.check(R.id.rfid_radiobutton)
        } else {
            binding.readerModeRadioGroup?.check(R.id.barcode_radiobutton)
        }
        binding.readerModeRadioGroup?.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rfid_radiobutton -> Global.setRfidMode()
                R.id.barcode_radiobutton -> Global.setBarcodeMode()
            }
        }

        homeViewModel.loadCompanies()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.companies.collect { companies: List<Company> ->
                    val names = companies.map { it.companyName ?: "" }
                    val adapter = ArrayAdapter(
                        requireContext(),
                        R.layout.item_dropdown,
                        names
                    )
                    binding.companyAutocompleteTextview?.setAdapter(adapter)

                    val itemHeight = 48
                    val maxVisibleItems = 5
                    val visibleItems = minOf(companies.size, maxVisibleItems)
                    val density = resources.displayMetrics.density
                    val dropdownHeight = (itemHeight * visibleItems * density).toInt()
                    binding.companyAutocompleteTextview?.dropDownHeight = dropdownHeight

                    val saved = AppSettings.tempSelectedSystem
                    if (saved != null) {
                        val match = companies.find { it.companyId == saved.companyId }
                        if (match != null) {
                            binding.companyAutocompleteTextview?.setText(
                                match.companyName ?: "", false
                            )
                        }
                    }
                    else if (companies.isNotEmpty()) {
                        val firstCompany = companies.first()

                        binding.companyAutocompleteTextview?.setText(
                            firstCompany.companyName ?: "", false
                        )
                        AppSettings.tempSelectedSystem = firstCompany
                    }
                }
            }
        }
        binding.companyAutocompleteTextview?.setOnItemClickListener { _, _, position, _ ->
            val company: Company = homeViewModel.companies.value[position]
            AppSettings.tempSelectedSystem = company
            AppSettings.tempSelectedLocation = null
        }

        binding.searchAssetButton.setOnClickListener {
            findNavController().navigate(R.id.nav_search_asset)
        }
        binding.movementButton.setOnClickListener {
            findNavController().navigate(R.id.nav_movement)
        }
        binding.readerListButton.setOnClickListener {
            findNavController().navigate(R.id.nav_reader_list)
        }
        binding.loadShipmentButton.setOnClickListener {
            findNavController().navigate(R.id.nav_load_shipment)
        }
        binding.searchShipmentButton.setOnClickListener {
            findNavController().navigate(R.id.nav_search_shipment)
        }
        binding.floorSweepButton.setOnClickListener {
            findNavController().navigate(R.id.nav_floor_sweep)
        }

        val fromLogin = arguments?.getBoolean("fromLogin", false) ?: false
        if (fromLogin) {
            startAutoSync()
        }

        return binding.root
    }

    private fun startAutoSync() {
        viewLifecycleOwner.lifecycleScope.launch {
            setSyncLoading(true, "Syncing in progress...")
            kotlinx.coroutines.delay(100)
            try {
                val success = SyncService().sync()
                if (success) {
                    setSyncLoading(true, "Sync successful! ")
                    kotlinx.coroutines.delay(1000)
                    setSyncLoading(false)
                } else {
                    setSyncLoading(false)
                    Toast.makeText(requireContext(), "Sync failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                setSyncLoading(false)
                Toast.makeText(requireContext(), "Sync failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setSyncLoading(isLoading: Boolean, message: String = "") {
        binding.syncLoaderOverlay?.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (message.isNotEmpty()) binding.syncLoaderMessage?.text = message
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}