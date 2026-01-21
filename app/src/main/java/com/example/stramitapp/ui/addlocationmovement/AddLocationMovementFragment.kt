package com.example.stramitapp.ui.addlocationmovement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentAddLocationMovementBinding

class AddLocationMovementFragment : Fragment() {

    private var _binding: FragmentAddLocationMovementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val addLocationMovementViewModel =
            ViewModelProvider(this).get(AddLocationMovementViewModel::class.java)

        _binding = FragmentAddLocationMovementBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.nextButton.setOnClickListener {
            findNavController().navigate(R.id.action_nav_movement_to_nav_movement_scanned_items)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}