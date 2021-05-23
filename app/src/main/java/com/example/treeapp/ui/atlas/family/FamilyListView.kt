package com.example.treeapp.ui.atlas.family

import com.arellomobile.mvp.MvpView
import com.example.treeapp.ui.atlas.PlantViewPagerAdapter


interface FamilyListView: MvpView {
    fun setAdapter(familyListAdapter: FamilyListAdapter)
}