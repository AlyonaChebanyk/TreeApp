package com.example.treeapp.ui.atlas.genus

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.treeapp.R
import com.example.treeapp.entities.Family
import com.example.treeapp.entities.Genus
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.plant_list_item.view.*

class GenusListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(genus: Genus) {
        with(view) {
            plantNameTextView.text = genus.name
            plantDescriptionTextView.text =
                if (genus.description.length < 200)
                    genus.description
                else
                    genus.description.slice(0..200) + "..."
            val imageUrl = genus.images.shuffled().take(1)[0]
            Picasso.get().load(imageUrl).into(plantImageView)

            setOnClickListener {
                val bundle = bundleOf("genus" to genus)
                findNavController().navigate(R.id.action_atlasFragment_to_genusPageFragment, bundle)
            }
        }
    }
}