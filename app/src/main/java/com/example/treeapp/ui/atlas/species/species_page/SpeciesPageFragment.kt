package com.example.treeapp.ui.atlas.species.species_page

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
import com.example.treeapp.entities.Species
import com.example.treeapp.ui.atlas.ImageListAdapter
import com.google.api.Distribution
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_species_page.*
import org.koin.android.ext.android.get

class SpeciesPageFragment : MvpAppCompatFragment(), SpeciesPageView {

    @InjectPresenter
    lateinit var presenter: SpeciesPagePresenter

    @ProvidePresenter
    fun provideSpeciesPagePresenter() = get<SpeciesPagePresenter>()

    private lateinit var species: Species

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            species = it.getParcelable("species")!!
            presenter.setData(species)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_species_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_speciesPageFragment_to_atlasFragment)
        }
    }

    override fun setImage(url: String) {
        Picasso.get().load(url).into(speciesImageView)
    }

    override fun setDescription(description: String) {
        speciesDescriptionTextView.text = description
    }

    override fun setFamily(family: String) {
        familyNameTextView.text = family
    }

    override fun setGenus(genus: String) {
        genusNameTextView.text = genus
    }

    override fun hideBarkLayout() {
        barkLayout.visibility = View.GONE
    }

    override fun hideFlowerLayout() {
        flowerLayout.visibility = View.GONE
    }

    override fun hideFruitLayout() {
        fruitLayout.visibility = View.GONE
    }

    override fun hideHabitLayout() {
        habitLayout.visibility = View.GONE
    }

    override fun hideLeafLayout() {
        leafLayout.visibility = View.GONE
    }

    override fun setBarkAdapter(barkAdapter: ImageListAdapter) {
        barkRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = barkAdapter
        }
    }

    override fun setFlowerAdapter(flowerAdapter: ImageListAdapter) {
        flowerRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = flowerAdapter
        }
    }

    override fun setFruitAdapter(fruitAdapter: ImageListAdapter) {
        fruitRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = fruitAdapter
        }
    }

    override fun setHabitAdapter(habitAdapter: ImageListAdapter) {
        habitRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = habitAdapter
        }
    }

    override fun setLeafAdapter(leafAdapter: ImageListAdapter) {
        leafRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = leafAdapter
        }
    }
}