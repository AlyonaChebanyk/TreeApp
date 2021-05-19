package com.example.treeapp.ui.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.treeapp.R
import com.example.treeapp.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_urban_environment_influance.*

class UrbanEnvironmentInfluenceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_urban_environment_influance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sideNavigationButton.setOnClickListener {
            (activity as MainActivity).showDrawer()
        }
    }

}