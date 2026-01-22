package com.example.stramitapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.stramitapp.Global
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Initialize radio group with current global state
        if (Global.isRfidSelected) {
            binding.scanTypeRadiogroup.check(R.id.rfid_radiobutton)
        } else {
            binding.scanTypeRadiogroup.check(R.id.barcode_radiobutton)
        }

        // Setup radio group listener
        binding.scanTypeRadiogroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rfid_radiobutton -> Global.setRfidMode()
                R.id.barcode_radiobutton -> Global.setBarcodeMode()
            }
        }

        // Setup Click Listeners
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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}