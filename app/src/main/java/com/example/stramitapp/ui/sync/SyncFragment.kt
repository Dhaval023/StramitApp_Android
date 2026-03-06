////package com.example.stramitapp.ui.sync
////
////import android.os.Bundle
////import android.view.LayoutInflater
////import android.view.View
////import android.view.ViewGroup
////import androidx.fragment.app.Fragment
////import androidx.lifecycle.ViewModelProvider
////import com.example.stramitapp.databinding.FragmentSyncBinding
////
////class SyncFragment : Fragment() {
////
////    private var _binding: FragmentSyncBinding? = null
////
////    // This property is only valid between onCreateView and
////    // onDestroyView.
////    private val binding get() = _binding!!
////
////    override fun onCreateView(
////        inflater: LayoutInflater,
////        container: ViewGroup?,
////        savedInstanceState: Bundle?
////    ): View {
////        val syncViewModel =
////            ViewModelProvider(this).get(SyncViewModel::class.java)
////
////        _binding = FragmentSyncBinding.inflate(inflater, container, false)
////        val root: View = binding.root
////
////        return root
////    }
////
////    override fun onDestroyView() {
////        super.onDestroyView()
////        _binding = null
////    }
////}
//
//package com.example.stramitapp.ui.sync
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import com.example.stramitapp.databinding.FragmentSyncBinding
//
//class SyncFragment : Fragment() {
//
//    private var _binding: FragmentSyncBinding? = null
//    private val binding get() = _binding!!
//
//    private lateinit var viewModel: SyncViewModel
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//
//        viewModel = ViewModelProvider(this)[SyncViewModel::class.java]
//
//        _binding = FragmentSyncBinding.inflate(inflater, container, false)
//
//        observeViewModel()
//
//        setupButtons()
//
//        return binding.root
//    }
//
//    private fun observeViewModel() {
//
//        viewModel.date.observe(viewLifecycleOwner) {
//            binding.dateTextview.text = it
//        }
//
//        viewModel.time.observe(viewLifecycleOwner) {
//            binding.timeTextview.text = it
//        }
//    }
//
//    private fun setupButtons() {
//
//        binding.syncButton.setOnClickListener {
//
//            viewModel.startSync { success ->
//
//                if (success) {
//                    Toast.makeText(requireContext(), "Sync Successful", Toast.LENGTH_LONG).show()
//                } else {
//                    Toast.makeText(requireContext(), "Sync Failed", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//
//        binding.forceSyncButton.setOnClickListener {
//
//            viewModel.startSync { success ->
//
//                if (success) {
//                    Toast.makeText(requireContext(), "Force Sync Successful", Toast.LENGTH_LONG).show()
//                } else {
//                    Toast.makeText(requireContext(), "Force Sync Failed", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}

package com.example.stramitapp.ui.sync

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stramitapp.databinding.FragmentSyncBinding

class SyncFragment : Fragment() {

    private var _binding: FragmentSyncBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SyncViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[SyncViewModel::class.java]

        _binding = FragmentSyncBinding.inflate(inflater, container, false)

        observeViewModel()
        setupButtons()

        return binding.root
    }

    private fun observeViewModel() {

        viewModel.date.observe(viewLifecycleOwner) {
            binding.dateTextview.text = it
        }

        viewModel.time.observe(viewLifecycleOwner) {
            binding.timeTextview.text = it
        }

        viewModel.isSyncing.observe(viewLifecycleOwner) { syncing ->
            binding.syncButton.isEnabled = !syncing
            binding.forceSyncButton.isEnabled = !syncing
        }
    }

    private fun setupButtons() {

        // Normal Sync
        binding.syncButton.setOnClickListener {

            viewModel.startSync(false) { success ->

                if (success) {
                    Toast.makeText(requireContext(), "Sync Successful", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Sync Failed", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Force Sync
        binding.forceSyncButton.setOnClickListener {

            AlertDialog.Builder(requireContext())
                .setTitle("Force Sync")
                .setMessage("This will download and replace the entire database. Continue?")
                .setPositiveButton("Yes") { _, _ ->

                    viewModel.startSync(true) { success ->

                        if (success) {
                            Toast.makeText(requireContext(), "Force Sync Successful", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(requireContext(), "Force Sync Failed", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}