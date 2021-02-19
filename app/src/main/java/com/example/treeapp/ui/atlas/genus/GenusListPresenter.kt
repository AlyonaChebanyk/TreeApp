package com.example.treeapp.ui.atlas.genus

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.treeapp.Repository
import com.example.treeapp.entities.Family
import com.example.treeapp.entities.Genus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@InjectViewState
class GenusListPresenter(private val repository: Repository): MvpPresenter<GenusListView>() {

    private lateinit var genusListAdapter: GenusListAdapter

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        GlobalScope.launch {
            genusListAdapter = GenusListAdapter(repository.getGenusList().shuffled() as MutableList<Genus>)
            withContext(Dispatchers.Main){
                viewState.setAdapter(genusListAdapter)
            }
        }
    }

}