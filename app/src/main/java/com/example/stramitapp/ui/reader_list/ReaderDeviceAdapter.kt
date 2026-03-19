package com.example.stramitapp.ui.reader_list

import android.graphics.Color
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

    private var selectedReaderName: String? = null

    fun setSelectedReader(readerName: String?) {
        selectedReaderName = readerName
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ReaderDeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reader_device, parent, false)
        return ReaderDeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReaderDeviceViewHolder, position: Int) {
        val readerDevice = getItem(position)
        holder.bind(readerDevice, readerDevice.name == selectedReaderName)
    }

    inner class ReaderDeviceViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val readerNameTextView: TextView = itemView.findViewById(R.id.textViewReaderName)
        private val readerSerialTextView: TextView = itemView.findViewById(R.id.textViewReaderSerial)
        private val readerModelTextView: TextView = itemView.findViewById(R.id.textViewReaderModel)
        private val rootLayout: View = itemView.findViewById(R.id.reader_box_layout)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val readerDevice = getItem(position)
                    listener.onItemClick(readerDevice)
                }
            }
        }

        fun bind(readerDevice: ReaderDevice, isSelected: Boolean) {
            readerNameTextView.text = readerDevice.name
            readerSerialTextView.text = readerDevice.serialNumber
            readerModelTextView.text = readerDevice.name

            if (isSelected) {
                rootLayout.setBackgroundResource(R.color.app_blue) // Darker blue when selected
                readerNameTextView.setTextColor(Color.WHITE)
                // Update other text colors if needed
            } else {
                rootLayout.setBackgroundResource(R.color.reader_list_item_blue)
                readerNameTextView.setTextColor(itemView.context.getColor(R.color.text_color_primary))
            }
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
            return oldItem.name == newItem.name && oldItem.serialNumber == newItem.serialNumber
        }
    }
}
