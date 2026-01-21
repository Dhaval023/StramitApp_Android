package com.example.stramitapp.ui.search_asset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stramitapp.databinding.FragmentSearchResultBinding

class SearchResultFragment : DialogFragment() {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
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

        val searchResultAdapter = SearchResultAdapter()
        binding.searchResultRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchResultAdapter
        }

        val dummyData = listOf(
            SearchResultItem("KN", "0011208303", "P-ACC", "BGPB100GA", "393144020406088132"),
            SearchResultItem("KN", "0011208303", "P-COR", "C42XWDG", "4299510-001"),
            SearchResultItem("KN", "Erp-2221", "Box", "C32F0", "3035EBA64400AD2ECE2C32F0"),
            SearchResultItem("KN", "Erp-2219", "Box", "C32F7", "3035EBA64400AB2ECE2C32F7"),
            SearchResultItem("KN", "Erp-2215", "Box", "C2BFE", "3035EBA64400ABAECE2C2BFE"),
            SearchResultItem("KN", "Erp-2214", "Box", "C2BEE", "3035EBA64400ABAECE2C2BEE"),
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