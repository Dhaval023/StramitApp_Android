package com.example.stramitapp.ui.search_shipment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stramitapp.databinding.ItemShipmentSearchResultBinding

class ShipmentSearchResultAdapter : ListAdapter<ShipmentSearchResultItem, ShipmentSearchResultAdapter.ShipmentSearchResultViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipmentSearchResultViewHolder {
        val binding = ItemShipmentSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShipmentSearchResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShipmentSearchResultViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ShipmentSearchResultViewHolder(private val binding: ItemShipmentSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShipmentSearchResultItem) {
            binding.shipmentIdTextview.text = item.shipmentId
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ShipmentSearchResultItem>() {
        override fun areItemsTheSame(oldItem: ShipmentSearchResultItem, newItem: ShipmentSearchResultItem): Boolean {
            return oldItem.shipmentId == newItem.shipmentId
        }

        override fun areContentsTheSame(oldItem: ShipmentSearchResultItem, newItem: ShipmentSearchResultItem): Boolean {
            return oldItem == newItem
        }
    }
}