package com.example.treeapp.ui.atlas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.example.treeapp.R
import com.example.treeapp.ui.atlas.family.FamilyListFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_atlas.*
import org.koin.android.ext.android.get

class AtlasFragment : Fragment(){

    private lateinit var plantViewPagerAdapter: PlantViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_atlas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        backToMainPageButton.setOnClickListener {
//            findNavController().navigate(R.id.action_atlasFragment_to_mainPageFragment)
//        }

        plantSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                plantViewPagerAdapter.searchText = newText ?: ""
                plantViewPagerAdapter.notifyDataSetChanged()
                return false
            }
        })

        plantViewPagerAdapter = PlantViewPagerAdapter(requireActivity())
        plantListViewPager.adapter = plantViewPagerAdapter
        TabLayoutMediator(plantTabLayout, plantListViewPager){ tab, position ->
            when (position) {
                0 -> tab.text = "Family"
                1 -> tab.text = "Genus"
                2 -> tab.text = "Species"
            }
        }.attach()
    }
}