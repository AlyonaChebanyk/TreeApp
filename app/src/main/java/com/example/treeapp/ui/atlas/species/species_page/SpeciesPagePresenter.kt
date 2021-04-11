package com.example.treeapp.ui.atlas.species.species_page

import android.media.Image
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.treeapp.entities.Genus
import com.example.treeapp.entities.Species
import com.example.treeapp.network.ImageList
import com.example.treeapp.ui.atlas.ImageListAdapter
import com.example.treeapp.ui.atlas.ListAdapter

@InjectViewState
class SpeciesPagePresenter : MvpPresenter<SpeciesPageView>() {

    private lateinit var barkAdapter: ImageListAdapter
    private lateinit var flowerAdapter: ImageListAdapter
    private lateinit var fruitAdapter: ImageListAdapter
    private lateinit var habitAdapter: ImageListAdapter
    private lateinit var leafAdapter: ImageListAdapter

    fun setData(species: Species) {
        viewState.setImage(species.imageUrl)
        viewState.setDescription(species.description)
        viewState.setGenus(species.genus)
        viewState.setFamily(species.family)

        if (species.images.habit.isNullOrEmpty())
            viewState.hideHabitLayout()
        else{
            habitAdapter = ImageListAdapter(species.images.habit)
            habitAdapter.notifyDataSetChanged()
        }

        if (species.images.bark.isNullOrEmpty())
            viewState.hideBarkLayout()
        else{
            barkAdapter = ImageListAdapter(species.images.bark)
            barkAdapter.notifyDataSetChanged()
        }

        if (species.images.flower.isNullOrEmpty())
            viewState.hideFlowerLayout()
        else{
            flowerAdapter = ImageListAdapter(species.images.flower)
            flowerAdapter.notifyDataSetChanged()
        }

        if (species.images.fruit.isNullOrEmpty())
            viewState.hideFruitLayout()
        else{
            fruitAdapter = ImageListAdapter(species.images.fruit)
            fruitAdapter.notifyDataSetChanged()
        }

        if (species.images.leaf.isNullOrEmpty())
            viewState.hideLeafLayout()
        else{
            leafAdapter = ImageListAdapter(species.images.leaf)
            leafAdapter.notifyDataSetChanged()
        }

//        viewState.setData(species)
//        val speciesList = mutableListOf<String>()
//        for (species in species.species)
//            for ((_, name) in species)
//                speciesList.add(name)
//        speciesListAdapter = ListAdapter(speciesList)
//        viewState.setSpeciesListAdapter(speciesListAdapter)
//        speciesListAdapter.notifyDataSetChanged()
//        imageListAdapter = ImageListAdapter(species.images.shuffled().take(10))
//        viewState.setImageListAdapter(imageListAdapter)
//        imageListAdapter.notifyDataSetChanged()
    }
}