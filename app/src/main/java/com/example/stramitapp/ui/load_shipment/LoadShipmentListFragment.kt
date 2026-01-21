package com.example.stramitapp.ui.load_shipment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stramitapp.databinding.FragmentLoadShipmentListBinding

class LoadShipmentListFragment : Fragment() {

    private var _binding: FragmentLoadShipmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val loadShipmentListViewModel =
            ViewModelProvider(this).get(LoadShipmentListViewModel::class.java)

        _binding = FragmentLoadShipmentListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}