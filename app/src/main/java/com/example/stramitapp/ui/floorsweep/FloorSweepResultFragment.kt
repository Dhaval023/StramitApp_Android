package com.example.stramitapp.ui.floorsweep

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentFloorSweepResultBinding
import com.example.stramitapp.model.FloorSweepResultListModel
import com.example.stramitapp.ui.locate_rfid.LocateTagRfidFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FloorSweepResultFragment : Fragment() {

    private var _binding: FragmentFloorSweepResultBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FloorSweepResultViewModel by viewModels()
    private lateinit var adapter: FloorSweepResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFloorSweepResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        handleArguments()

        binding.closeIcon.setOnClickListener {
            viewModel.closePageEvent()
        }
    }

    private fun setupRecyclerView() {
        adapter = FloorSweepResultAdapter(emptyList()) { item ->
            viewModel.selectResultEvent(item)
        }
        binding.floorSweepRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.floorSweepRecyclerview.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.floorSweepResultList.observe(viewLifecycleOwner) { results ->
            adapter.updateData(results)
        }

        viewModel.navigateToLocate.observe(viewLifecycleOwner) { pair ->
            pair ?: return@observe

            val (rfid, barcode) = pair

            if (findNavController().currentDestination?.id != R.id.nav_floor_sweep_result) {
                Log.w("FloorSweepResult", "Navigation skipped — wrong destination")
                return@observe
            }

            val bundle = Bundle().apply {
                putString(LocateTagRfidFragment.ARG_TAG, rfid)
                putString(LocateTagRfidFragment.ARG_BARCODE, barcode)
            }

            try {
                findNavController().navigate(
                    R.id.action_nav_floor_sweep_result_to_nav_locate_tag_rfid,
                    bundle
                )
            } catch (e: Exception) {
                Log.e("FloorSweepResult", "Navigation to locate failed: ${e.message}")
            }

            viewModel.onNavigationHandled()
        }

        viewModel.navigateToHome.observe(viewLifecycleOwner) { shouldGoHome ->
            if (shouldGoHome) {
                findNavController().navigate(
                    R.id.nav_home,
                    null,
                    androidx.navigation.NavOptions.Builder()
                        .setPopUpTo(R.id.nav_home, true)
                        .build()
                )
                viewModel.onNavigationHandled()
            }
        }
    }

    private fun handleArguments() {
        arguments?.getString("results_json")?.let { json ->
            val type = object : TypeToken<List<FloorSweepResultListModel>>() {}.type
            val results: List<FloorSweepResultListModel> = Gson().fromJson(json, type)
            viewModel.setResults(results)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}