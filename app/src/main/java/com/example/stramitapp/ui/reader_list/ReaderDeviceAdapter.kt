package com.example.stramitapp.ui.reader_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stramitapp.R
import com.zebra.rfid.api3.ReaderDevice

class ReaderDeviceAdapter(private val listener: OnItemClickListener) :
    ListAdapter<ReaderDevice, ReaderDeviceAdapter.ReaderDeviceViewHolder>(ReaderDeviceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ReaderDeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reader_device, parent, false)
        return ReaderDeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReaderDeviceViewHolder, position: Int) {
        val readerDevice = getItem(position)
        holder.bind(readerDevice)
    }

    inner class ReaderDeviceViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val readerNameTextView: TextView = itemView.findViewById(R.id.textViewReaderName)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val readerDevice = getItem(position)
                    listener.onItemClick(readerDevice)
                }
            }
        }

        fun bind(readerDevice: ReaderDevice) {
            readerNameTextView.text = readerDevice.name
        }
    }

    interface OnItemClickListener {
        fun onItemClick(readerDevice: ReaderDevice)
    }

    class ReaderDeviceDiffCallback : DiffUtil.ItemCallback<ReaderDevice>() {
        override fun areItemsTheSame(oldItem: ReaderDevice, newItem: ReaderDevice):
                Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ReaderDevice, newItem: ReaderDevice):
                Boolean {
            return oldItem == newItem
        }
    }
}