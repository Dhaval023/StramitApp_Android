package com.example.stramitapp.ui.reader_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stramitapp.MainActivity
import com.example.stramitapp.databinding.FragmentReaderListBinding
import com.example.stramitapp.zebraconnection.RFIDHandler
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

        val rfidHandler = (requireActivity() as MainActivity).getRfidHandler()

        viewModel.checkCurrentConnection(rfidHandler)

        viewModel.getAvailableReaders(rfidHandler)

        binding.scanButton.setOnClickListener {
            viewModel.getAvailableReaders(rfidHandler)
        }

        observeViewModel()
    }
    private fun setupRecyclerView() {
        adapter = ReaderDeviceAdapter(object : ReaderDeviceAdapter.OnItemClickListener {
            override fun onItemClick(readerDevice: ReaderDevice) {
                val rfidHandler = (requireActivity() as MainActivity).getRfidHandler()
                if (viewModel.connectedReaderName.value == readerDevice.name && 
                    RFIDHandler.mConnectedRfidReader?.isConnected == true) {
                    Toast.makeText(requireContext(), "Already connected to ${readerDevice.name}", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.connectToReader(rfidHandler, readerDevice)
                    Toast.makeText(requireContext(), "Connecting to ${readerDevice.name}...", Toast.LENGTH_SHORT).show()
                }
            }
        })
        binding.readerList.adapter = adapter
        binding.readerList.layoutManager = LinearLayoutManager(requireContext())
    }
    private fun observeViewModel() {
        viewModel.readerList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            if (it.isEmpty()) {
                Toast.makeText(requireContext(), "No readers found. Please check Bluetooth.", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.connectedReaderName.observe(viewLifecycleOwner) { name ->
            adapter.setSelectedReader(name)
        }

        viewModel.isScanning.observe(viewLifecycleOwner) { isScanning ->
            binding.scanButton.isEnabled = !isScanning
            binding.scanButton.text = if (isScanning) "Scanning..." else "Scan for Readers"
        }

        (requireActivity() as MainActivity).getRfidHandler()?.connectionStatus?.observe(viewLifecycleOwner) { isConnected ->
            if (!isConnected) {
                viewModel.updateConnectedReader(null)
            } else {
                viewModel.checkCurrentConnection((requireActivity() as MainActivity).getRfidHandler())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}