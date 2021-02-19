package com.example.treeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.treeapp.R
import kotlinx.android.synthetic.main.fragment_main_page.*

class MainPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        testTextView.setOnClickListener {
            findNavController().navigate(R.id.action_mainPageFragment_to_testFragment)
        }
        atlasTextView.setOnClickListener {
            findNavController().navigate(R.id.action_mainPageFragment_to_atlasFragment)
        }
    }
}