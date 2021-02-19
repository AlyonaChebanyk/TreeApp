package com.example.treeapp.ui.atlas.species

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.treeapp.entities.Genus
import com.example.treeapp.entities.Species
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.plant_list_item.view.*

class GenusListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(species: Species) {
        with(view) {
            plantNameTextView.text =
                if (!species.commonName.isNullOrEmpty())
                    species.commonName
                else
                    species.scientificName
            plantDescriptionTextView.text =
                if (species.description.length < 200)
                    species.description
                else
                    species.description.slice(0..200) + "..."
            Picasso.get().load(species.imageUrl).into(plantImageView)
        }
    }
}