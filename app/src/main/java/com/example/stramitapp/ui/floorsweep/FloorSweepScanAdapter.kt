package com.example.stramitapp.ui.floorsweep

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stramitapp.databinding.ItemFloorSweepScanBinding
import com.example.stramitapp.model.FloorSweepModel

class FloorSweepScanAdapter(
    private val onDeleteItem: (FloorSweepModel) -> Unit = {}
) : ListAdapter<FloorSweepModel, FloorSweepScanAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(val binding: ItemFloorSweepScanBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFloorSweepScanBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.rfidText.text = item.rfid
        holder.binding.root.setOnLongClickListener {
            onDeleteItem(item)
            true
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<FloorSweepModel>() {
        override fun areItemsTheSame(oldItem: FloorSweepModel, newItem: FloorSweepModel) =
            oldItem.rfid == newItem.rfid

        override fun areContentsTheSame(oldItem: FloorSweepModel, newItem: FloorSweepModel) =
            oldItem.rfid == newItem.rfid
    }
}