package com.example.stramitapp.ui.search_shipment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stramitapp.databinding.ItemShipmentSearchResultBinding

class ShipmentSearchResultAdapter(
    private val onItemClick: (ShipmentSearchResultItem) -> Unit
) : ListAdapter<ShipmentSearchResultItem, ShipmentSearchResultAdapter.ViewHolder>(DIFF) {

    inner class ViewHolder(private val b: ItemShipmentSearchResultBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(item: ShipmentSearchResultItem) {
            b.locationNameTextview.text   = item.locationName
            b.custom13Textview.text       = item.custom13
            b.custom22Textview.text       = item.custom22
            b.companyAssetIdTextview.text = item.companyAssetId
            b.custom17Textview.text       = item.custom17
            b.barcodeTextview.text        = item.barcode

            b.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemShipmentSearchResultBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ShipmentSearchResultItem>() {
            override fun areItemsTheSame(
                a: ShipmentSearchResultItem, b: ShipmentSearchResultItem
            ) = a.assetId == b.assetId

            override fun areContentsTheSame(
                a: ShipmentSearchResultItem, b: ShipmentSearchResultItem
            ) = a == b
        }
    }
}