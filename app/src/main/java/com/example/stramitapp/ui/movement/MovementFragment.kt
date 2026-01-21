package com.example.stramitapp.ui.movement

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stramitapp.MainActivity
import com.example.stramitapp.databinding.FragmentMovementBinding
import com.example.stramitapp.zebraconnection.RFIDHandler
import com.example.stramitapp.zebraconnection.Inventory.TagDataViewModel

class MovementFragment : Fragment() {

    private var _binding: FragmentMovementBinding? = null
    private val binding get() = _binding!!

    private var rfidHandler: RFIDHandler? = null
    private lateinit var tagDataViewModel: TagDataViewModel
    private var scannedListAdapter: ScannedListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rfidHandler = (requireActivity() as? MainActivity)?.getRfidHandler()
        tagDataViewModel = ViewModelProvider(requireActivity()).get(TagDataViewModel::class.java)

        // Initialize adapter with empty list
        scannedListAdapter = ScannedListAdapter(mutableListOf()) { position ->
            showDeleteSingleConfirmation(position)
        }
        binding.scannedList.adapter = scannedListAdapter

        // Delete all button click listener
        binding.deleteAll.setOnClickListener {
            showDeleteAllConfirmation()
        }

        rfidHandler?.triggerPressedLiveData?.observe(viewLifecycleOwner) { isPressed ->
            if (isPressed) {
                rfidHandler?.performInventory()
            } else {
                rfidHandler?.stopInventory()
            }
        }

        tagDataViewModel.getInventoryItem().observe(viewLifecycleOwner) { items ->
            Log.d("MovementFragment", "Items received: ${items.size}")

            val tagIds = items.map { it.tagID }

            // Log each tag
            tagIds.forEachIndexed { index, tag ->
                Log.d("MovementFragment", "Item $index: $tag")
            }

            // Update adapter with new data
            scannedListAdapter?.updateData(tagIds)

            // Update count
            binding.itemCount.text = if (items.size == 1) "1 item" else "${items.size} items"
        }
    }

    private fun showDeleteAllConfirmation() {
        val itemCount = scannedListAdapter?.getCount() ?: 0
        if (itemCount == 0) {
            Toast.makeText(requireContext(), "No items to delete", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Delete All Items")
            .setMessage("Are you sure you want to delete all $itemCount items?")
            .setPositiveButton("Delete") { _, _ ->
                scannedListAdapter?.clearAll()
                binding.itemCount.text = "0 item"
                Toast.makeText(requireContext(), "All items deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteSingleConfirmation(position: Int) {
        val tagId = scannedListAdapter?.getItem(position) ?: return

        AlertDialog.Builder(requireContext())
            .setTitle("Delete Item")
            .setMessage("Delete this item?\n\n$tagId")
            .setPositiveButton("Delete") { _, _ ->
                scannedListAdapter?.removeItem(position)
                val count = scannedListAdapter?.getCount() ?: 0
                binding.itemCount.text = if (count == 1) "1 item" else "$count items"
                Toast.makeText(requireContext(), "Item deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}