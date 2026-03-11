package com.example.stramitapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.stramitapp.Global
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentHomeBinding
import com.example.stramitapp.services.SyncService
import com.example.stramitapp.ui.login.LoginViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Logged in user
        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.authenticatedUser.collect { user ->
                Log.d("HomeFragment", "user received: $user")
                val firstName = user?.firstName ?: ""
                val lastName  = user?.lastName  ?: ""
                val fullName  = "$firstName $lastName".trim()
                binding.fullName = fullName.ifEmpty { "" }
            }
        }

        // Radio group
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

        // Click listeners
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

        // ── Only sync when coming from Login ──────────────────────────────
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
                    setSyncLoading(true, "Sync successful! ✓")
                    kotlinx.coroutines.delay(1500)
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