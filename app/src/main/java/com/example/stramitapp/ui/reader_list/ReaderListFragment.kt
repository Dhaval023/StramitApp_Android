package com.example.stramitapp.ui.reader_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stramitapp.MainActivity
import com.example.stramitapp.databinding.FragmentReaderListBinding
import com.zebra.rfid.api3.ReaderDevice

class ReaderListFragment : Fragment() {

    private var _binding: FragmentReaderListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ReaderListViewModel
    private lateinit var adapter: ReaderDeviceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReaderListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ReaderListViewModel::class.java)

        setupRecyclerView()

        binding.scanButton.setOnClickListener {
            viewModel.getAvailableReaders((requireActivity() as MainActivity).getRfidHandler())
        }

        observeReaderList()
    }

    private fun setupRecyclerView() {
        adapter = ReaderDeviceAdapter(object : ReaderDeviceAdapter.OnItemClickListener {
            override fun onItemClick(readerDevice: ReaderDevice) {
                viewModel.connectToReader((requireActivity() as MainActivity).getRfidHandler(), readerDevice)
            }
        })
        binding.readerList.adapter = adapter
        binding.readerList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeReaderList() {
        viewModel.readerList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}