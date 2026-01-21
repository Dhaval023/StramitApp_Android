package com.example.stramitapp.zebraconnection.connect;

import static com.example.stramitapp.zebraconnection.RFIDHandler.mConnectedRfidReader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stramitapp.MainActivity;
import com.example.stramitapp.databinding.FragmentConnectBinding;
import com.example.stramitapp.zebraconnection.RFIDHandler;

import java.util.ArrayList;

public class ConnectFragment extends Fragment {

    private FragmentConnectBinding binding;
    private ReaderDeviceAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ConnectViewModel connectViewModel =
                new ViewModelProvider(this).get(ConnectViewModel.class);

        binding = FragmentConnectBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RFIDHandler handler = ((MainActivity) requireActivity()).getRfidHandler();
        RecyclerView recyclerView = binding.recyclerViewReaders;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (handler != null) {
            handler.isInitialized.observe(getViewLifecycleOwner(), isInitialized -> {
                if (Boolean.TRUE.equals(isInitialized)) {
                    android.util.Log.d(RFIDHandler.TAG, "RFIDHandler is  initialized ");
                    if (!connectViewModel.hasRefreshed()) {
                        connectViewModel.refreshReaders();
                    }
                } else {
                    android.util.Log.d(RFIDHandler.TAG, "RFIDHandler is not initialized yet");
                }
            });
        }


        // Set up Refresh button click listener
        binding.buttonRefreshReaders.setOnClickListener(v -> {
            connectViewModel.refreshReaders();
        });
        adapter = new ReaderDeviceAdapter(new ArrayList<>(), device -> {

            handler.selectReader(device);
            // Disconnect if already connected, otherwise connect
            if (mConnectedRfidReader != null && mConnectedRfidReader.isConnected()) {
                handler.disconnect();
            } else {
                handler.connect(device.getName());
            }
        });
        recyclerView.setAdapter(adapter);

        connectViewModel.getReaders().observe(getViewLifecycleOwner(), readerDevices -> {
            android.util.Log.d("ConnectFragment", "Observed readers: " + (readerDevices != null ? readerDevices.size() : "null"));
            if (readerDevices != null) {
                for (int i = 0; i < readerDevices.size(); i++) {
                    android.util.Log.d("ConnectFragment", "Observed Reader[" + i + "]: " + readerDevices.get(i).getName());
                }
            }
            adapter.setReaderList(readerDevices);
        });

        // Observe RFIDHandler initialization before refreshing readers


        // Observe connection status and show feedback
        if (handler != null && handler.connectionStatus != null) {
            handler.connectionStatus.observe(getViewLifecycleOwner(), isConnected -> {
                if (isConnected == null) return;
                String msg = isConnected ? "RFID Reader Connected" : "RFID Reader Connection Failed";
                android.widget.Toast.makeText(requireContext(), msg, android.widget.Toast.LENGTH_SHORT).show();
                // Update the connection status TextView
//                if (binding != null && binding.connectionStatusText != null) {
//                    binding.connectionStatusText.setText(isConnected ? "Connected" : "Disconnected");
//                }
                if (isConnected && mConnectedRfidReader != null) {
                    adapter.setConnectedReaderName(mConnectedRfidReader.getHostName());
                } else {
                    adapter.setConnectedReaderName(null);
                }
            });
        } else {
            // Optionally log or show a message if handler is null
            android.util.Log.e(RFIDHandler.TAG, "RFIDHandler or connectionStatus is null");
        }

        if (mConnectedRfidReader != null && mConnectedRfidReader.isConnected()) {
            adapter.setConnectedReaderName(mConnectedRfidReader.getHostName());
           // binding.connectionStatusText.setText("Connected" );
        } else {
            adapter.setConnectedReaderName(null);
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        // Dispose the RFIDHandler when the fragment is destroyed
//        RFIDHandler handler = ((com.example.stramitapp.MainActivity) requireActivity()).getRfidHandler();
//        if (handler != null) {
//            handler.dispose();
//        }
    }
}