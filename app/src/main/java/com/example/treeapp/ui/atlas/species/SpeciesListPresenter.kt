package com.example.treeapp.ui.atlas.species

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.treeapp.Repository
import com.example.treeapp.entities.Genus
import com.example.treeapp.entities.Species
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@InjectViewState
class SpeciesListPresenter(private val repository: Repository): MvpPresenter<SpeciesListView>() {

    private lateinit var speciesListAdapter: SpeciesListAdapter

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        GlobalScope.launch {
            speciesListAdapter = SpeciesListAdapter(repository.getSpecies().shuffled() as MutableList<Species>)
            withContext(Dispatchers.Main){
                viewState.setAdapter(speciesListAdapter)
            }
        }
    }

}