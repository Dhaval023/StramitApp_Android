package com.example.stramitapp.ui.floorsweep

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stramitapp.databinding.ItemFloorSweepResultBinding
import com.example.stramitapp.model.FloorSweepResultListModel

class FloorSweepResultAdapter(
    private var items: List<FloorSweepResultListModel>,
    private val onItemClick: (FloorSweepResultListModel) -> Unit
) : RecyclerView.Adapter<FloorSweepResultAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemFloorSweepResultBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFloorSweepResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            idText.text = item.id
            coText.text = item.co
            deliveryDateText.text = item.deliveryDate
            productSkuText.text = item.productSKU
            
            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<FloorSweepResultListModel>) {
        items = newItems
        notifyDataSetChanged()
    }
}
