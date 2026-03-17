package com.example.stramitapp.ui.search_shipment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.stramitapp.databinding.FragmentSearchShipmentBinding
import kotlinx.coroutines.launch

class SearchShipmentFragment : Fragment() {

    private var _binding: FragmentSearchShipmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SearchShipmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        android.util.Log.d("SHIPMENT_DEBUG", "onCreateView called")
        viewModel = ViewModelProvider(this)[SearchShipmentViewModel::class.java]
        _binding = FragmentSearchShipmentBinding.inflate(inflater, container, false)

        setupSearchResults()
        setupButtons()

        return binding.root
    }

    private fun setupSearchResults() {
        // Loading spinner
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect { isLoading ->
                    binding.searchButton.isEnabled = !isLoading
                    binding.progressBar.visibility =
                        if (isLoading) View.VISIBLE else View.GONE
                }
            }
        }

        // Error toast
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorMessage.collect { message ->
                    if (message.isNotEmpty()) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Results — open dialog
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchResults.collect { results ->
                    android.util.Log.d("SHIPMENT_DEBUG", "Collected results: ${results.size}")
                    if (results.isNotEmpty()) {
                        ShipmentSearchResultFragment
                            .newInstance(ArrayList(results))
                            .show(childFragmentManager, ShipmentSearchResultFragment.TAG)
                    }
                }
            }
        }
    }

    private fun setupButtons() {
        android.util.Log.d("SHIPMENT_DEBUG", "setupButtons() called")
        binding.searchButton.setOnClickListener {
            android.util.Log.d("SHIPMENT_DEBUG", "Search button CLICKED")

            val shipmentNumber = binding.shipmentIdEdittext.text?.toString()?.trim() ?: ""
            val m3CO           = binding.m3CoEdittext.text?.toString()?.trim() ?: ""
            val m3DO           = binding.m3DoEdittext.text?.toString()?.trim() ?: ""

            android.util.Log.d("SHIPMENT_DEBUG", "shipmentNumber=$shipmentNumber m3CO=$m3CO m3DO=$m3DO")

            viewModel.search(
                shipmentNumber = shipmentNumber,
                m3CO           = m3CO,
                m3DO           = m3DO
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}