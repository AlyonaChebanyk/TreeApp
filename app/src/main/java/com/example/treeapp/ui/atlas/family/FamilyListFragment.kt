package com.example.treeapp.ui.atlas.family

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

class FamilyListFragment : MvpAppCompatFragment(), FamilyListView {

    @InjectPresenter
    lateinit var presenter: FamilyListPresenter

    @ProvidePresenter
    fun provideFamilyListPresenter() = get<FamilyListPresenter>()

    private val searchText = arguments?.getString("searchText") ?:""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plant_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.loadData(searchText)
    }

    override fun setAdapter(familyListAdapter: FamilyListAdapter) {
        plantListRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = familyListAdapter
        }
    }
}