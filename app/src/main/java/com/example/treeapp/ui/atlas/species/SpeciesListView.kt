package com.example.treeapp.ui.atlas.species

import com.arellomobile.mvp.MvpView


interface SpeciesListView: MvpView {
    fun setAdapter(speciesListAdapter: SpeciesListAdapter)
}