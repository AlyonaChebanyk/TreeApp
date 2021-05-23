package com.example.treeapp.ui.atlas.genus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.treeapp.R
import com.example.treeapp.entities.Genus

class GenusListAdapter(private val genusList: MutableList<Genus>): RecyclerView.Adapter<GenusListViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenusListViewHolder {
        return GenusListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.plant_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GenusListViewHolder, position: Int) {
        holder.bind(genusList[position])
    }

    override fun getItemCount(): Int = genusList.size

}