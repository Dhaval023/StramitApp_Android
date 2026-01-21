package com.example.stramitapp.ui.movement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.stramitapp.R

class ScannedListAdapter(
    private val tagIds: MutableList<String>,
    private val onDeleteClick: (Int) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = tagIds.size

    override fun getItem(position: Int): String = tagIds[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scanned, parent, false)

        val tagIdTextView = view.findViewById<TextView>(R.id.tag_id_text_view)
        val deleteButton = view.findViewById<ImageView>(R.id.delete_item)

        tagIdTextView.text = tagIds[position]

        deleteButton.setOnClickListener {
            onDeleteClick(position)
        }

        return view
    }

    fun updateData(newData: List<String>) {
        tagIds.clear()
        tagIds.addAll(newData)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (position in 0 until tagIds.size) {
            tagIds.removeAt(position)
            notifyDataSetChanged()
        }
    }

    fun clearAll() {
        tagIds.clear()
        notifyDataSetChanged()
    }
}