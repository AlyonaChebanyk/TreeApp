package com.example.treeapp.ui.atlas.family.family_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.treeapp.R
import com.example.treeapp.entities.Family
import com.example.treeapp.ui.atlas.ImageListAdapter
import com.example.treeapp.ui.atlas.ListAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_family_page.*
import org.koin.android.ext.android.get

class FamilyPageFragment : MvpAppCompatFragment(), FamilyPageView {

    @InjectPresenter
    lateinit var presenter: FamilyPagePresenter

    @ProvidePresenter
    fun provideFamilyPagePresenter() = get<FamilyPagePresenter>()

    private lateinit var family: Family

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            family = it.getParcelable("family")!!
            presenter.setData(family)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_family_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_familyPageFragment_to_atlasFragment)
        }
    }

    override fun setImage(url: String) {
        Picasso.get().load(url).into(familyImageView)
    }

    override fun setGenusListAdapter(genusAdapter: ListAdapter) {
        genusListRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = genusAdapter
        }
    }

    override fun setImageListAdapter(imageListAdapter: ImageListAdapter) {
        familyImagesRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = imageListAdapter
        }
    }

    override fun setData(family: Family) {
        familyNameTextView.text = if (!family.commonName.isNullOrEmpty()) family.commonName
        else
            family.name
        familyDescriptionTextView.text = family.description
        familyCommonNameTextView.text = family.commonName
        familyScientificNameTextView.text = family.name
    }
}