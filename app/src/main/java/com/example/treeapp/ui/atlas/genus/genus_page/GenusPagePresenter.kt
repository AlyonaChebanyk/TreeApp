package com.example.treeapp.ui.atlas.genus.genus_page

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.treeapp.entities.Genus
import com.example.treeapp.ui.atlas.ImageListAdapter
import com.example.treeapp.ui.atlas.ListAdapter

@InjectViewState
class GenusPagePresenter : MvpPresenter<GenusPageView>() {

    private lateinit var speciesListAdapter: ListAdapter
    private lateinit var imageListAdapter: ImageListAdapter

    fun setData(genus: Genus) {
        viewState.setImage(genus.images.shuffled()[0])
        viewState.setData(genus)
        val speciesList = mutableListOf<String>()
        for (species in genus.species)
            for ((_, name) in species)
                speciesList.add(name)
        speciesListAdapter = ListAdapter(speciesList)
        viewState.setSpeciesListAdapter(speciesListAdapter)
        speciesListAdapter.notifyDataSetChanged()
        imageListAdapter = ImageListAdapter(genus.images.shuffled().take(10))
        viewState.setImageListAdapter(imageListAdapter)
        imageListAdapter.notifyDataSetChanged()
    }
}