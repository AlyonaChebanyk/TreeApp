package com.example.treeapp.ui.atlas.family

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.treeapp.entities.Family
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.plant_list_item.view.*

class FamilyListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(family: Family) {
        with(view) {
            plantNameTextView.text =
                if (!family.commonName.isNullOrEmpty())
                    family.commonName
                else
                    family.name
            plantDescriptionTextView.text =
                if (family.description.length < 200)
                    family.description
                else
                    family.description.slice(0..200) + "..."
            val imageUrl = family.images.shuffled().take(1)[0]
            Picasso.get().load(imageUrl).into(plantImageView)
        }
    }
}