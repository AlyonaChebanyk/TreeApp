package com.example.treeapp.ui.atlas.species.species_page

import android.media.Image
import com.arellomobile.mvp.MvpView
import com.example.treeapp.entities.Family
import com.example.treeapp.entities.Genus
import com.example.treeapp.ui.atlas.ImageListAdapter
import com.example.treeapp.ui.atlas.ListAdapter

interface SpeciesPageView : MvpView {
    fun setImage(url: String)
    fun setDescription(description: String)
    fun setGenus(genus: String)
    fun setFamily(family: String)

    fun hideFlowerLayout()
    fun hideLeafLayout()
    fun hideHabitLayout()
    fun hideFruitLayout()
    fun hideBarkLayout()

    fun setFlowerAdapter(flowerAdapter: ImageListAdapter)
    fun setLeafAdapter(leafAdapter: ImageListAdapter)
    fun setHabitAdapter(habitAdapter: ImageListAdapter)
    fun setFruitAdapter(fruitAdapter: ImageListAdapter)
    fun setBarkAdapter(barkAdapter: ImageListAdapter)
}