package com.example.treeapp.ui.atlas

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_item.view.*
import timber.log.Timber

class ImageListViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    fun bind(url: String){
        Timber.d("Image url: $url")
        Picasso.get().load(url).into(view.imageView)
    }
}