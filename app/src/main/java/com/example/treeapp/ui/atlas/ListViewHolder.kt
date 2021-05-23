package com.example.treeapp.ui.atlas

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: String) {
        view.itemTextView.text = item
    }
}