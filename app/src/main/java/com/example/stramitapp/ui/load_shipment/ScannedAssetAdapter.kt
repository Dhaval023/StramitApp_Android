package com.example.stramitapp.ui.load_shipment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.stramitapp.R
import com.example.stramitapp.model.Asset

class ScannedAssetAdapter(
    context: Context,
    private val resource: Int,
    private val items: MutableList<Asset>,
    private val onDeleteClickListener: (Asset) -> Unit
) : ArrayAdapter<Asset>(context, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
        val asset = getItem(position)

        val textView = view.findViewById<TextView>(R.id.tag_id_text_view)
        val deleteIcon = view.findViewById<ImageView>(R.id.delete_item)

        textView.text = asset?.title ?: asset?.barcode ?: "Unknown"

        deleteIcon.setOnClickListener {
            asset?.let { onDeleteClickListener(it) }
        }

        return view
    }
}