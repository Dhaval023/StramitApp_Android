package com.example.stramitapp.ui.movement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.stramitapp.R
import com.example.stramitapp.model.Asset

class ScannedListAdapter(
    private val items: MutableList<Asset>,
    private val onDeleteClick: (Int) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Asset = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scanned, parent, false)

        val asset = items[position]

        val tagIdTextView = view.findViewById<TextView>(R.id.tag_id_text_view)
        val deleteButton = view.findViewById<ImageView>(R.id.delete_item)

        tagIdTextView.text = when {
            !asset.title.isNullOrBlank() -> asset.title
            !asset.barcode.isNullOrBlank() -> asset.barcode
            !asset.tag.isNullOrBlank() -> asset.tag
            else -> "Unknown"
        }

        deleteButton.setOnClickListener {
            onDeleteClick(position)
        }

        return view
    }

    fun updateData(newData: List<Asset>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (position in 0 until items.size) {
            items.removeAt(position)
            notifyDataSetChanged()
        }
    }

    fun clearAll() {
        items.clear()
        notifyDataSetChanged()
    }
}