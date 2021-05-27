package com.example.treeapp.ui.atlas

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.example.treeapp.ui.atlas.family.FamilyListFragment
import com.example.treeapp.ui.atlas.genus.GenusListFragment
import com.example.treeapp.ui.atlas.species.SpeciesListFragment


class PlantViewPagerAdapter(val activity: FragmentActivity): FragmentStateAdapter(activity) {
//    var familyFragment = FamilyListFragment()
//    var genusFragment = GenusListFragment()

    var searchText = ""

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val bundle = bundleOf("searchText" to searchText)
        return when (position) {
            0 -> FamilyListFragment().apply {
                arguments = bundle
            }
            1 -> GenusListFragment().apply {
                arguments = bundle
            }
            2 -> SpeciesListFragment().apply {
                arguments = bundle
            }
            else -> Fragment()
        }
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun containsItem(itemId: Long): Boolean {
        return arrayListOf(0, 1, 2).any { it.toLong() == itemId }
    }

    override fun onBindViewHolder(
        holder: FragmentViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)

        createFragment(position)

        //manual update fragment
    }
}