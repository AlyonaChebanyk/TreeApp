package com.example.treeapp.ui.atlas.family.family_page

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.treeapp.entities.Family
import com.example.treeapp.ui.atlas.ImageListAdapter
import com.example.treeapp.ui.atlas.ListAdapter
import timber.log.Timber

@InjectViewState
class FamilyPagePresenter: MvpPresenter<FamilyPageView>() {

    private lateinit var genusListAdapter: ListAdapter
    private lateinit var imageListAdapter: ImageListAdapter

    fun setData(family: Family){
        viewState.setImage(family.images.shuffled()[0])
        viewState.setData(family)
        genusListAdapter = ListAdapter(family.genus)
        viewState.setGenusListAdapter(genusListAdapter)
        genusListAdapter.notifyDataSetChanged()
        imageListAdapter = ImageListAdapter(family.images.shuffled().take(10))
        viewState.setImageListAdapter(imageListAdapter)
        imageListAdapter.notifyDataSetChanged()
    }

}