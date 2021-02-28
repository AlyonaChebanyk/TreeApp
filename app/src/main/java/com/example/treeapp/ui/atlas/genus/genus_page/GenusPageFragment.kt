package com.example.treeapp.ui.atlas.genus.genus_page

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
import com.example.treeapp.entities.Genus
import com.example.treeapp.ui.atlas.ImageListAdapter
import com.example.treeapp.ui.atlas.ListAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_genus_page.*
import org.koin.android.ext.android.get

class GenusPageFragment : MvpAppCompatFragment(), GenusPageView {

    @InjectPresenter
    lateinit var presenter: GenusPagePresenter

    @ProvidePresenter
    fun provideGenusPagePresenter() = get<GenusPagePresenter>()

    private lateinit var genus: Genus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            genus = it.getParcelable("genus")!!
            presenter.setData(genus)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_genus_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_genusPageFragment_to_atlasFragment)
        }
    }

    override fun setImage(url: String) {
        Picasso.get().load(url).into(genusImageView)
    }

    override fun setSpeciesListAdapter(genusAdapter: ListAdapter) {
        genusListRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = genusAdapter
        }
    }

    override fun setImageListAdapter(imageListAdapter: ImageListAdapter) {
        genusImagesRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = imageListAdapter
        }
    }

    override fun setData(genus: Genus) {
        genusNameTextView.text = genus.name
        familyNameTextView.text = genus.familyCommonName
    }
}