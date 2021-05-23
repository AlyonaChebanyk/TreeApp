package com.example.treeapp.ui.atlas.family

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.treeapp.Repository
import com.example.treeapp.entities.Family
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@InjectViewState
class FamilyListPresenter(private val repository: Repository): MvpPresenter<FamilyListView>() {

    private lateinit var familyListAdapter: FamilyListAdapter

//    override fun onFirstViewAttach() {
//        super.onFirstViewAttach()
//        GlobalScope.launch {
//            familyListAdapter = FamilyListAdapter(repository.getFamilies().shuffled() as MutableList<Family>)
//            withContext(Dispatchers.Main){
//                viewState.setAdapter(familyListAdapter)
//            }
//        }
//    }

    fun loadData(searchText: String){
        GlobalScope.launch {
            familyListAdapter = FamilyListAdapter(repository.getFamilies().shuffled().filter {
                if (!it.commonName.isNullOrEmpty())
                    it.commonName.contains(searchText, ignoreCase = true)
                else
                    it.name.contains(searchText, ignoreCase = true)
            } as MutableList<Family>)
            withContext(Dispatchers.Main){
                viewState.setAdapter(familyListAdapter)
            }
        }
    }
}