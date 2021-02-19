package com.example.treeapp.ui.atlas.genus

import com.arellomobile.mvp.MvpView


interface GenusListView: MvpView {
    fun setAdapter(genusListAdapter: GenusListAdapter)
}