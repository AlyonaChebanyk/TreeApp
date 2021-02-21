package com.example.treeapp.ui.atlas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.treeapp.R

class ImageListAdapter(private val imageList: List<String>) :
    RecyclerView.Adapter<ImageListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageListViewHolder {
        return ImageListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageListViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount(): Int = imageList.size
}