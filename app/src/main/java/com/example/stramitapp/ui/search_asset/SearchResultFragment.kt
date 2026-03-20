package com.example.stramitapp.ui.search_asset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentSearchResultBinding

class SearchResultFragment : DialogFragment() {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_RESULTS = "search_results"
        const val TAG = "SearchResultFragment"

        fun newInstance(results: ArrayList<SearchResultItem>): SearchResultFragment {
            val fragment = SearchResultFragment()
            val args = Bundle()
            args.putSerializable(ARG_RESULTS, results)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val screenHeight = resources.displayMetrics.heightPixels
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            (screenHeight * 0.90).toInt()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("UNCHECKED_CAST")
        val results = arguments?.getSerializable(ARG_RESULTS) as? ArrayList<SearchResultItem>
            ?: arrayListOf()

        val adapter = SearchResultAdapter { item ->
            openDetailPage(item)
        }

        binding.searchResultRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
            addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
        }

        adapter.submitList(results)
        binding.itemCountTextview.text = "${results.size} items"

        binding.closeButton.setOnClickListener { dismiss() }
    }

    private fun openDetailPage(item: SearchResultItem) {
        dismiss()

        val bundle = Bundle()
        bundle.putSerializable("asset_item", item)
        requireParentFragment()
            .findNavController()
            .navigate(R.id.nav_asset_detail, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}