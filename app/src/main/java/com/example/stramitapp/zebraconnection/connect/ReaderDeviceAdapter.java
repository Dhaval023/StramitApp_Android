package com.example.stramitapp.zebraconnection.connect;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stramitapp.R;
import com.zebra.rfid.api3.ReaderDevice;
import java.util.List;

public class ReaderDeviceAdapter extends RecyclerView.Adapter<ReaderDeviceAdapter.ReaderViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(ReaderDevice device);
    }

    private List<ReaderDevice> readerList;
    private final OnItemClickListener listener;
    private String connectedReaderName = null;

    public ReaderDeviceAdapter(List<ReaderDevice> readerList, OnItemClickListener listener) {
        this.readerList = readerList;
        this.listener = listener;
    }

    public void setReaderList(List<ReaderDevice> readerList) {
        this.readerList = readerList;
        notifyDataSetChanged();
    }

    public void setConnectedReaderName(String name) {
        connectedReaderName = name;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reader_device, parent, false);
        return new ReaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReaderViewHolder holder, int position) {
        ReaderDevice device = readerList.get(position);
        holder.bind(device);
    }

    @Override
    public int getItemCount() {
        return readerList != null ? readerList.size() : 0;
    }

    class ReaderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewSerial;
        TextView textViewModel;

        ReaderViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewReaderName);
            textViewSerial = itemView.findViewById(R.id.textViewReaderSerial);
            textViewModel = itemView.findViewById(R.id.textViewReaderModel);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(readerList.get(position));
                }
            });
        }

        void bind(ReaderDevice device) {
            textViewName.setText(device.getName());
            textViewSerial.setText("Serial: " + device.getSerialNumber());
            textViewModel.setText("Model: " + device.getName());
        }
    }
}