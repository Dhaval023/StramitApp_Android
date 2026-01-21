package com.example.stramitapp.ui.search_asset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stramitapp.databinding.FragmentSearchAssetBinding

class SearchAssetFragment : Fragment() {

    private var _binding: FragmentSearchAssetBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val searchAssetViewModel =
            ViewModelProvider(this).get(SearchAssetViewModel::class.java)

        _binding = FragmentSearchAssetBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.searchButton.setOnClickListener {
            val searchResultFragment = SearchResultFragment()
            searchResultFragment.show(childFragmentManager, "SearchResultFragment")
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}