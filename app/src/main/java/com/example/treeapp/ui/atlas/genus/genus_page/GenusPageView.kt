package com.example.treeapp.ui.atlas.genus.genus_page

import com.arellomobile.mvp.MvpView
import com.example.treeapp.entities.Family
import com.example.treeapp.entities.Genus
import com.example.treeapp.ui.atlas.ImageListAdapter
import com.example.treeapp.ui.atlas.ListAdapter

interface GenusPageView: MvpView {
    fun setData(genus: Genus)
    fun setImage(url: String)
    fun setSpeciesListAdapter(genusAdapter: ListAdapter)
    fun setImageListAdapter(imageListAdapter: ImageListAdapter)
}