package com.example.stramitapp.ui.search_shipment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentShipmentSearchResultBinding
import com.example.stramitapp.ui.search_asset.AssetDetailFragment

class ShipmentSearchResultFragment : DialogFragment() {

    private var _binding: FragmentShipmentSearchResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShipmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("DEPRECATION")
        val results: List<ShipmentSearchResultItem> =
            arguments?.getParcelableArrayList<ShipmentSearchResultItem>(ARG_RESULTS)
                ?: emptyList()

        val adapter = ShipmentSearchResultAdapter { item ->
            val searchResultItem = item.toSearchResultItem()
            dismiss()

            // Navigate to the common AssetDetailFragment
            val bundle = Bundle().apply {
                putSerializable("asset_item", searchResultItem)
            }
            findNavController().navigate(R.id.nav_asset_detail, bundle)
        }

        binding.searchResultRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter  = adapter
        }
        adapter.submitList(results)
        binding.itemCountTextview.text = "${results.size} items"

        binding.closeButton.setOnClickListener { dismiss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ShipmentSearchResultFragment"
        private const val ARG_RESULTS = "arg_results"

        fun newInstance(results: ArrayList<ShipmentSearchResultItem>): ShipmentSearchResultFragment {
            return ShipmentSearchResultFragment().apply {
                arguments = Bundle().apply {
                    @Suppress("UNCHECKED_CAST")
                    putParcelableArrayList(ARG_RESULTS, results as ArrayList<out android.os.Parcelable>)
                }
            }
        }
    }
}
