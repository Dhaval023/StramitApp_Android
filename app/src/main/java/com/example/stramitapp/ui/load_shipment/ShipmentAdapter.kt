package com.example.stramitapp.ui.load_shipment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stramitapp.databinding.ItemScannedBinding
import com.example.stramitapp.model.Asset

class ShipmentAdapter(
    private val onDelete: (String) -> Unit
) : RecyclerView.Adapter<ShipmentAdapter.VH>() {

    private val list = mutableListOf<String>()

    fun submit(newList: List<String>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class VH(val binding: ItemScannedBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemScannedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = list[position]

        holder.binding.tagIdTextView.text = item

        holder.binding.deleteItem.setOnClickListener {
            onDelete(item)
        }
    }

    override fun getItemCount() = list.size
}