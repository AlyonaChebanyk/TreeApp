package com.example.treeapp.ui.atlas.family

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.treeapp.R
import com.example.treeapp.entities.Family

class FamilyListAdapter(private val familyList: MutableList<Family>): RecyclerView.Adapter<FamilyListViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamilyListViewHolder {
        return FamilyListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.plant_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FamilyListViewHolder, position: Int) {
        holder.bind(familyList[position])
    }

    override fun getItemCount(): Int = familyList.size

}