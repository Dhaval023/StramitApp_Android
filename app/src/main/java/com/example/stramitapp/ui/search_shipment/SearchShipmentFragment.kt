package com.example.stramitapp.ui.search_shipment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stramitapp.databinding.FragmentSearchShipmentBinding

class SearchShipmentFragment : Fragment() {

    private var _binding: FragmentSearchShipmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val searchShipmentViewModel =
            ViewModelProvider(this).get(SearchShipmentViewModel::class.java)

        _binding = FragmentSearchShipmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.searchButton.setOnClickListener {
            val searchResultFragment = ShipmentSearchResultFragment()
            searchResultFragment.show(childFragmentManager, "ShipmentSearchResultFragment")
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}