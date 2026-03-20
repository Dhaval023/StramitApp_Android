package com.example.stramitapp.ui.load_shipment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentShipmentItemResultBinding
import com.example.stramitapp.assetinfo.AssetDetailFragment
import com.example.stramitapp.ui.search_shipment.ShipmentSearchResultAdapter
import com.example.stramitapp.ui.search_shipment.ShipmentSearchResultItem
import com.example.stramitapp.ui.search_shipment.toSearchResultItem
import kotlinx.coroutines.launch

class ShipmentItemResultFragment : Fragment() {

    private var _binding: FragmentShipmentItemResultBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ShipmentItemResultViewModel by viewModels()

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShipmentItemResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shipmentNumber = arguments?.getString(ARG_SHIPMENT_NUMBER).orEmpty()

        setupToolbar()
        setupRecyclerView()
        observeViewModel()

         viewModel.loadResults(shipmentNumber)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ── Setup ─────────────────────────────────────────────────────────────────

    private fun setupToolbar() {
        binding.btnClose.setOnClickListener {
            try {
                val popped = findNavController().popBackStack(R.id.nav_home, false)
                if (!popped) {
                    requireActivity().onBackPressed()
                }
            } catch (e: Exception) {
                android.util.Log.e("ShipmentItemResult", "Nav error: ${e.message}")
                requireActivity().onBackPressed()
            }
        }
    }

    private fun setupRecyclerView() {
        val adapter = ShipmentSearchResultAdapter { item ->
            navigateToAssetDetail(item)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter  = adapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collect { items ->
                    adapter.submitList(items)
                    binding.itemCountTextview.text = "${items.size} items"
                }
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect { loading ->
                    binding.loaderLayout.visibility = if (loading) View.VISIBLE else View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isEmpty.collect { empty ->
                    binding.recyclerView.visibility     = if (empty) View.GONE else View.VISIBLE
                    binding.tvNoItems.visibility        = if (empty) View.VISIBLE else View.GONE
                }
            }
        }
    }
    // ── Navigation ────────────────────────────────────────────────────────────
    private fun navigateToAssetDetail(item: ShipmentSearchResultItem) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.nav_host_fragment_content_main,
                AssetDetailFragment.newInstance(item.toSearchResultItem())
            )
            .addToBackStack(null)
            .commit()
    }

    companion object {
        private const val ARG_SHIPMENT_NUMBER = "shipment_number"

        fun newInstance(shipmentNumber: String): ShipmentItemResultFragment {
            return ShipmentItemResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SHIPMENT_NUMBER, shipmentNumber)
                }
            }
        }
    }
}