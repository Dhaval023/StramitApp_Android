package com.example.stramitapp.ui.search_asset

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stramitapp.databinding.ItemSearchResultBinding

class SearchResultAdapter(
    private val onItemClick: (SearchResultItem) -> Unit
) : ListAdapter<SearchResultItem, SearchResultAdapter.SearchResultViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val binding = ItemSearchResultBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SearchResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SearchResultViewHolder(
        private val binding: ItemSearchResultBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SearchResultItem) {
            binding.knTextview.text   = item.locationName
            binding.codeTextview.text = item.companyAssetId
            binding.pAccTextview.text = item.custom13
            binding.bgpbTextview.text = item.barcode
            binding.idTextview.text   = item.custom18
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<SearchResultItem>() {
        override fun areItemsTheSame(old: SearchResultItem, new: SearchResultItem) =
            old.assetId == new.assetId
        override fun areContentsTheSame(old: SearchResultItem, new: SearchResultItem) =
            old == new
    }
}