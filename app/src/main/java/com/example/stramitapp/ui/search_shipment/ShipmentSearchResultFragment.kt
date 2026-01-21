package com.example.stramitapp.ui.search_shipment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stramitapp.databinding.FragmentShipmentSearchResultBinding

class ShipmentSearchResultFragment : DialogFragment() {

    private var _binding: FragmentShipmentSearchResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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

        val searchResultAdapter = ShipmentSearchResultAdapter()
        binding.searchResultRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchResultAdapter
        }

        val dummyData = listOf(
            ShipmentSearchResultItem("6133213"),
            ShipmentSearchResultItem("M3 CO"),
            ShipmentSearchResultItem("M3 DO"),
        )
        searchResultAdapter.submitList(dummyData)
        binding.itemCountTextview.text = "${dummyData.size} items"

        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}