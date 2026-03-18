package com.example.stramitapp.ui.floorsweep

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentFloorSweepBinding
import com.example.stramitapp.zebraconnection.Inventory.TagDataViewModel
import com.google.gson.Gson
import com.zebra.rfid.api3.TagData
import java.text.SimpleDateFormat
import java.util.*

class FloorSweepFragment : Fragment() {

    private var _binding: FragmentFloorSweepBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FloorSweepViewModel by viewModels()
    private val tagDataViewModel: TagDataViewModel by viewModels({ requireActivity() })

    private val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFloorSweepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        setupObservers()
        setupListeners()
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.floor_sweep_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_submit -> {
                        viewModel.submitEvent()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupObservers() {
        viewModel.floorSweepDateEntry.observe(viewLifecycleOwner) { date ->
            binding.dateEdittext.setText(sdf.format(date))
        }

        viewModel.isDatePickerEnable.observe(viewLifecycleOwner) { enabled ->
            binding.dateEdittext.isEnabled = enabled
            binding.saveButton.isEnabled = enabled
        }

        viewModel.scannedItemsList.observe(viewLifecycleOwner) { list ->
            val countText = "${list.size} item${if (list.size != 1) "s" else ""}"
            binding.itemCountTextview.text = countText
            // Update your list adapter here if you have one
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                viewModel.clearErrorMessage()
            }
        }

        viewModel.navigationToResult.observe(viewLifecycleOwner) { resultList ->
            resultList?.let {
                val bundle = Bundle().apply {
                    putString("results_json", Gson().toJson(it))
                }
                findNavController().navigate(R.id.action_nav_floor_sweep_to_nav_floor_sweep_result, bundle)
                viewModel.onNavigationDone()
            }
        }

        tagDataViewModel.inventoryItem.observe(viewLifecycleOwner) { tags: Array<TagData>? ->
            tags?.let {
                viewModel.onTagRead(it)
            }
        }
    }

    private fun setupListeners() {
        binding.dateEdittext.setOnClickListener {
            if (viewModel.isDatePickerEnable.value == true) {
                showDatePicker()
            }
        }

        binding.saveButton.setOnClickListener {
            showSaveConfirmation()
        }

        binding.deleteIcon.setOnClickListener {
            showClearAllConfirmation()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        viewModel.floorSweepDateEntry.value?.let { calendar.time = it }

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)
                viewModel.setFloorSweepDate(selectedCalendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showSaveConfirmation() {
        val dateStr = sdf.format(viewModel.floorSweepDateEntry.value ?: Date())
        AlertDialog.Builder(requireContext())
            .setTitle("SAVE DATE")
            .setMessage("Are you sure you want to Select Date >>> \"$dateStr\" <<< ?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.saveDate()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun showClearAllConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("CLEAR ALL")
            .setMessage("Are you sure you want to clear all Scanned Items?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.clearAll()
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
