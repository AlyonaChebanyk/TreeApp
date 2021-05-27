package com.example.treeapp.ui.atlas.family

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.treeapp.R
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
            
            plantScientificNameTextView.text = family.name

            val imageUrl = family.images.shuffled().take(1)[0]
            Glide.with(view).load(imageUrl).into(plantImageView)

            setOnClickListener {
                val bundle = bundleOf("family" to family)
                findNavController().navigate(R.id.action_atlasFragment_to_familyPageFragment, bundle)
            }
        }
    }
}