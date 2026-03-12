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

    companion object {
        private const val ARG_RESULTS = "search_results"

        fun newInstance(results: ArrayList<SearchResultItem>): SearchResultFragment {
            val fragment = SearchResultFragment()
            val args = Bundle()
            args.putSerializable(ARG_RESULTS, results)
            fragment.arguments = args
            return fragment
        }
    }

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
        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val dialogHeight = (screenHeight * 0.75).toInt() // 75% of screen height

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            dialogHeight
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("UNCHECKED_CAST")
        val results = arguments?.getSerializable(ARG_RESULTS) as? ArrayList<SearchResultItem>
            ?: arrayListOf()

        val adapter = SearchResultAdapter()
        binding.searchResultRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        adapter.submitList(results)
        binding.itemCountTextview.text = "${results.size} items"

        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}