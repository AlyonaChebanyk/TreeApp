package com.example.treeapp.ui.atlas.species

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.treeapp.R
import com.example.treeapp.entities.Genus
import com.example.treeapp.entities.Species
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.plant_list_item.view.*
import timber.log.Timber

class SpeciesListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
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
            Glide.with(view).load(species.imageUrl).into(plantImageView)
            Timber.d(species.imageUrl)

            setOnClickListener {
                val bundle = bundleOf("species" to species)
                findNavController().navigate(R.id.action_atlasFragment_to_speciesPageFragment, bundle)
            }
        }
    }
}