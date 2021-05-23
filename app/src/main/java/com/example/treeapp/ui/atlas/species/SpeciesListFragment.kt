package com.example.treeapp.ui.atlas.species

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.treeapp.R
import kotlinx.android.synthetic.main.fragment_plant_list.*
import org.koin.android.ext.android.get

class SpeciesListFragment : MvpAppCompatFragment(), SpeciesListView {

    @InjectPresenter
    lateinit var presenter: SpeciesListPresenter

    @ProvidePresenter
    fun provideGenusListPresenter() = get<SpeciesListPresenter>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plant_list, container, false)
    }

    override fun setAdapter(speciesListAdapter: SpeciesListAdapter) {
        plantListRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = speciesListAdapter
        }
    }
}