package com.example.treeapp.ui.atlas.family.family_page

import com.arellomobile.mvp.MvpView
import com.example.treeapp.entities.Family
import com.example.treeapp.ui.atlas.ImageListAdapter
import com.example.treeapp.ui.atlas.ListAdapter

interface FamilyPageView: MvpView {
    fun setData(family: Family)
    fun setImage(url: String)
    fun setGenusListAdapter(genusAdapter: ListAdapter)
    fun setImageListAdapter(imageListAdapter: ImageListAdapter)
}