package com.example.treeapp.ui.atlas

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_item.view.*

class ImageListViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    fun bind(url: String){
        Picasso.get().load(url).into(view.imageView)
    }
}