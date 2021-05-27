package com.example.treeapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.treeapp.R
import com.example.treeapp.entities.Family2
import com.example.treeapp.entities.Genus2
import com.example.treeapp.entities.Images
import com.example.treeapp.entities.Species2
import com.example.treeapp.network.ApiService
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {

    private val service: ApiService by inject()
    val db = Firebase.firestore

    val RESULT_LOAD_IMG = 1
    var counter = 0

    //    private val CAMERA_REQUEST = 1888
//    private val MY_CAMERA_PERMISSION_CODE = 100
    val dataForDatabase = mutableListOf<Family2>()
    private lateinit var header: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        header = nav_view.getHeaderView(0)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigation.setupWithNavController(navController)
//        loadData()
//        loadGenus()
//        loadSpecies()

    }

    fun loadData() {
        val list = arrayListOf<String>()
        csvReader().open(assets.open("train.csv")) {
            readAllAsSequence().forEach { row ->
                if (row[1] != "species")
                    list.add(row[1])
            }
        }
//        val familyList = mutableListOf<String>()
//        val genusList = mutableListOf<String>()
        val speciesFullList = mutableListOf<String>()
        val familyMap = mutableMapOf<String, ArrayList<String>>()
        val familyImages = mutableMapOf<String, ArrayList<String>>()
        val genusMap = mutableMapOf<String, ArrayList<Pair<String, String>>>()
        val genusImages = mutableMapOf<String, ArrayList<String>>()
//        val dataForDatabase = mutableListOf<Family2>()
        GlobalScope.launch {
            for (item in list) {
                val name = item.replace("_", "-").toLowerCase()
                try {
//                    val data = service.getSpeciesInfo(name)
//                    if (!familyList.contains(data.species.family)) {
//                        familyList.add(data.species.family)
//                        dataForDatabase.add(
//                            Family2(
//                                data.species.family,
//                                data.species.familyCommonName,
//                                "",
//                                mutableListOf()
//                            ))
//                        for (family in dataForDatabase){
//                            db.collection("family").document(family.name).set(family)
//                        }
//                    }
//                    if (!genusList.contains(data.species.genus)) {
//                        genusList.add(data.species.genus)
//                        dataForDatabase.forEachIndexed { index, family2 ->
//                            if (family2.name == data.species.family){
//                                dataForDatabase[index].genus.add(
//                                    Genus2(
//                                        data.species.genus,
//                                        "",
//                                        mutableListOf()
//                                    )
//                                )
//                            }
//                        }
//                    }
//                    for (genus in dataForDatabase[familyIndex].genus)
//                        if (genus.equals(data.species.genus)){
//                            dataForDatabase[familyIndex].genus.indexOf(genus)
//                        }
//                    if (!speciesList.contains(data.species.scientificName)){
//                        speciesList.add(data.species.scientificName)
//                        dataForDatabase.forEachIndexed { familyIndex, family2 ->
//                            if (family2.name == data.species.family){
//                                dataForDatabase[familyIndex].genus.forEachIndexed { index, genus2 ->
//                                    if (genus2.name == data.species.genus) {
//                                        dataForDatabase[familyIndex].genus[index].species.add(
//                                            Species2(
//                                                data.species.commonName,
//                                                data.species.scientificName,
//                                                "",
//                                                data.species.imageUrl,
//                                                Images(
//                                                    habit = data.species.images.habit.map { it.imageUrl }
//                                                        .toMutableList(),
//                                                    leaf = data.species.images.leaf.map { it.imageUrl }
//                                                        .toMutableList(),
//                                                    flower = data.species.images.flower.map { it.imageUrl }
//                                                        .toMutableList(),
//                                                    fruit = data.species.images.fruit.map { it.imageUrl }
//                                                        .toMutableList(),
//                                                    bark = data.species.images.bark.map { it.imageUrl }
//                                                        .toMutableList()
//                                                )
//                                            )
//                                        )
//                                    }
//                                }
//                            }
//                        }
//
//                    }

                    val data = service.getSpeciesInfo(name)
                    if (!familyMap.contains(data.species.family)) {
                        familyMap[data.species.family] = arrayListOf()
                        familyImages[data.species.family] = arrayListOf()
                        db.collection("families").document(data.species.family).set(
                            hashMapOf(
                                "name" to data.species.family,
                                "commonName" to data.species.familyCommonName,
                                "description" to "",
                                "genus" to arrayListOf<String>(),
                                "images" to arrayListOf<String>()
                            )
                        )
                    }
                    if (!genusMap.contains(data.species.genus)) {
                        familyMap[data.species.family]!!.add(data.species.genus)
                        genusMap[data.species.genus] = arrayListOf()
                        genusImages[data.species.genus] = arrayListOf()

                        db.collection("genus").document(data.species.genus).set(
                            hashMapOf(
                                "name" to data.species.genus,
                                "family" to data.species.family,
                                "familyCommonName" to data.species.familyCommonName,
                                "description" to "",
                                "species" to arrayListOf<MutableMap<String, String>>(),
                                "images" to arrayListOf<String>()
                            )
                        )
                    }
                    if (!speciesFullList.contains(data.species.scientificName)) {
                        speciesFullList.add(data.species.scientificName)
                        genusMap[data.species.genus]!!.add(data.species.scientificName to data.species.commonName)
                        familyImages[data.species.family]!!.add(data.species.imageUrl)
                        genusImages[data.species.genus]!!.add(data.species.imageUrl)
                        db.collection("species").document(data.species.scientificName).set(
                            hashMapOf(
                                "commonName" to data.species.commonName,
                                "scientificName" to data.species.scientificName,
                                "genus" to data.species.genus,
                                "commonFamilyName" to data.species.familyCommonName,
                                "family" to data.species.family,
                                "imageUrl" to data.species.imageUrl,
                                "description" to "",
                                "images" to hashMapOf(
                                    "leaf" to data.species.images.leaf.map { it.imageUrl },
                                    "fruit" to data.species.images.fruit.map { it.imageUrl },
                                    "flower" to data.species.images.flower.map { it.imageUrl },
                                    "bark" to data.species.images.bark.map { it.imageUrl },
                                    "habit" to data.species.images.habit.map { it.imageUrl }
                                )
                            )
                        )
                    }
                } catch (e: Exception) {
                }

            }
//            for (family in dataForDatabase){
//                db.collection("family").document(family.name).set(family)
//            }


            for ((family, genusList) in familyMap) {
                db.collection("families").document(family).update("genus", genusList)
            }
            for ((family, images) in familyImages) {
                db.collection("families").document(family).update("images", images)
            }

            for ((genus, speciesList) in genusMap) {
                db.collection("genus").document(genus).update("species", speciesList)
            }
            for ((genus, images) in genusImages) {
                db.collection("genus").document(genus).update("images", images)
            }
        }
    }

    fun loadGenus() {
        val list = arrayListOf<String>()
        csvReader().open(assets.open("train.csv")) {
            readAllAsSequence().forEach { row ->
                if (row[1] != "species")
                    list.add(row[1])
            }
        }
//        val familyList = mutableListOf<String>()
        val genusList = mutableListOf<String>()
        val speciesList = mutableListOf<String>()
//        val familyMap = mutableMapOf<String, ArrayList<String>>()
//        val familyImages = mutableMapOf<String, ArrayList<String>>()
//        val genusMap = mutableMapOf<String, ArrayList<Pair<String, String>>>()
//        val genusImages = mutableMapOf<String, ArrayList<String>>()
//        val dataForDatabase = mutableListOf<Family2>()
        GlobalScope.launch {
            for (item in list) {
                val name = item.replace("_", "-").toLowerCase()
                try {
                    val data = service.getSpeciesInfo(name)
//                    if (!familyList.contains(data.species.family)) {
//                        familyList.add(data.species.family)
//                        dataForDatabase.add(
//                            Family2(
//                                data.species.family,
//                                data.species.familyCommonName,
//                                "",
//                                mutableListOf()
//                            )
//                        )
//                    }
                    if (!genusList.contains(data.species.genus)) {
                        genusList.add(data.species.genus)
                        dataForDatabase.forEachIndexed { index, family2 ->
                            if (family2.name == data.species.family) {
                                dataForDatabase[index].genus.add(
                                    Genus2(
                                        data.species.genus,
                                        "",
                                        mutableListOf()
                                    )
                                )
                            }
                        }
                        for (family in dataForDatabase) {
                            db.collection("family").document(family.name).set(family)
                        }
                    }
//                    for (genus in dataForDatabase[familyIndex].genus)
//                        if (genus.equals(data.species.genus)){
//                            dataForDatabase[familyIndex].genus.indexOf(genus)
//                        }
//                    if (!speciesList.contains(data.species.scientificName)){
//                        speciesList.add(data.species.scientificName)
//                        dataForDatabase.forEachIndexed { familyIndex, family2 ->
//                            if (family2.name == data.species.family){
//                                dataForDatabase[familyIndex].genus.forEachIndexed { index, genus2 ->
//                                    if (genus2.name == data.species.genus) {
//                                        dataForDatabase[familyIndex].genus[index].species.add(
//                                            Species2(
//                                                data.species.commonName,
//                                                data.species.scientificName,
//                                                "",
//                                                data.species.imageUrl,
//                                                Images(
//                                                    habit = data.species.images.habit.map { it.imageUrl }
//                                                        .toMutableList(),
//                                                    leaf = data.species.images.leaf.map { it.imageUrl }
//                                                        .toMutableList(),
//                                                    flower = data.species.images.flower.map { it.imageUrl }
//                                                        .toMutableList(),
//                                                    fruit = data.species.images.fruit.map { it.imageUrl }
//                                                        .toMutableList(),
//                                                    bark = data.species.images.bark.map { it.imageUrl }
//                                                        .toMutableList()
//                                                )
//                                            )
//                                        )
//                                    }
//                                }
//                            }
//                        }
//
//                    }

//                    val data = service.getSpeciesInfo(name)
//                    if (!familyMap.contains(data.species.family)) {
//                        familyMap[data.species.family] = arrayListOf()
//                        familyImages[data.species.family] = arrayListOf()
//                        db.collection("families").document(data.species.family).set(
//                            hashMapOf(
//                                "name" to data.species.family,
//                                "commonName" to data.species.familyCommonName,
//                                "description" to "",
//                                "genus" to arrayListOf<String>(),
//                                "images" to arrayListOf<String>()
//                            )
//                        )
//                    }
//                    if (!genusMap.contains(data.species.genus)) {
//                        familyMap[data.species.family]!!.add(data.species.genus)
//                        genusMap[data.species.genus] = arrayListOf()
//                        genusImages[data.species.genus] = arrayListOf()
//
//                        db.collection("genus").document(data.species.genus).set(
//                            hashMapOf(
//                                "name" to data.species.genus,
//                                "family" to data.species.family,
//                                "familyCommonName" to data.species.familyCommonName,
//                                "description" to "",
//                                "species" to arrayListOf<MutableMap<String, String>>(),
//                                "images" to arrayListOf<String>()
//                            )
//                        )
//                    }
//                    genusMap[data.species.genus]!!.add(data.species.scientificName to data.species.commonName)
//                    familyImages[data.species.family]!!.add(data.species.imageUrl)
//                    genusImages[data.species.genus]!!.add(data.species.imageUrl)
//                    db.collection("species").document(data.species.scientificName).set(
//                        hashMapOf(
//                            "commonName" to data.species.commonName,
//                            "scientificName" to data.species.scientificName,
//                            "genus" to data.species.genus,
//                            "commonFamilyName" to data.species.familyCommonName,
//                            "family" to data.species.family,
//                            "imageUrl" to data.species.imageUrl,
//                            "description" to "",
//                            "images" to hashMapOf(
//                                "leaf" to data.species.images.leaf,
//                                "fruit" to data.species.images.fruit,
//                                "flower" to data.species.images.flower,
//                                "bark" to data.species.images.bark,
//                                "habit" to data.species.images.habit
//                            )
//                        )
//                    )
                } catch (e: Exception) {
                }

            }
            for (family in dataForDatabase) {
                db.collection("family").document(family.name).set(family)
            }


//            for ((family, genusList) in familyMap) {
//                db.collection("families").document(family).update("genus", genusList)
//            }
//            for ((family, images) in familyImages) {
//                db.collection("families").document(family).update("images", images)
//            }
//
//            for ((genus, speciesList) in genusMap) {
//                db.collection("genus").document(genus).update("species", speciesList)
//            }
//            for ((genus, images) in genusImages) {
//                db.collection("genus").document(genus).update("images", images)
//            }
        }
    }

    fun loadSpecies() {
        val list = arrayListOf<String>()
        csvReader().open(assets.open("train.csv")) {
            readAllAsSequence().forEach { row ->
                if (row[1] != "species")
                    list.add(row[1])
            }
        }
//        val familyList = mutableListOf<String>()
//        val genusList = mutableListOf<String>()
        val speciesList = mutableListOf<String>()
//        val familyMap = mutableMapOf<String, ArrayList<String>>()
//        val familyImages = mutableMapOf<String, ArrayList<String>>()
//        val genusMap = mutableMapOf<String, ArrayList<Pair<String, String>>>()
//        val genusImages = mutableMapOf<String, ArrayList<String>>()
//        val dataForDatabase = mutableListOf<Family2>()
        GlobalScope.launch {
            for (item in list) {
                val name = item.replace("_", "-").toLowerCase()
                try {
                    val data = service.getSpeciesInfo(name)
//                    if (!familyList.contains(data.species.family)) {
//                        familyList.add(data.species.family)
//                        dataForDatabase.add(
//                            Family2(
//                                data.species.family,
//                                data.species.familyCommonName,
//                                "",
//                                mutableListOf()
//                            )
//                        )
//                    }
//                    if (!genusList.contains(data.species.genus)) {
//                        genusList.add(data.species.genus)
//                        dataForDatabase.forEachIndexed { index, family2 ->
//                            if (family2.name == data.species.family){
//                                dataForDatabase[index].genus.add(
//                                    Genus2(
//                                        data.species.genus,
//                                        "",
//                                        mutableListOf()
//                                    )
//                                )
//                            }
//                        }
//                    }
//                    for (genus in dataForDatabase[familyIndex].genus)
//                        if (genus.equals(data.species.genus)){
//                            dataForDatabase[familyIndex].genus.indexOf(genus)
//                        }
                    if (!speciesList.contains(data.species.scientificName)) {
                        speciesList.add(data.species.scientificName)
                        dataForDatabase.forEachIndexed { familyIndex, family2 ->
                            if (family2.name == data.species.family) {
                                dataForDatabase[familyIndex].genus.forEachIndexed { index, genus2 ->
                                    if (genus2.name == data.species.genus) {
                                        dataForDatabase[familyIndex].genus[index].species.add(
                                            Species2(
                                                data.species.commonName,
                                                data.species.scientificName,
                                                "",
                                                data.species.imageUrl,
                                                Images(
                                                    habit = data.species.images.habit.map { it.imageUrl }
                                                        .toMutableList(),
                                                    leaf = data.species.images.leaf.map { it.imageUrl }
                                                        .toMutableList(),
                                                    flower = data.species.images.flower.map { it.imageUrl }
                                                        .toMutableList(),
                                                    fruit = data.species.images.fruit.map { it.imageUrl }
                                                        .toMutableList(),
                                                    bark = data.species.images.bark.map { it.imageUrl }
                                                        .toMutableList()
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        for (family in dataForDatabase) {
                            db.collection("family").document(family.name).set(family)
                        }
                    }

//                    val data = service.getSpeciesInfo(name)
//                    if (!familyMap.contains(data.species.family)) {
//                        familyMap[data.species.family] = arrayListOf()
//                        familyImages[data.species.family] = arrayListOf()
//                        db.collection("families").document(data.species.family).set(
//                            hashMapOf(
//                                "name" to data.species.family,
//                                "commonName" to data.species.familyCommonName,
//                                "description" to "",
//                                "genus" to arrayListOf<String>(),
//                                "images" to arrayListOf<String>()
//                            )
//                        )
//                    }
//                    if (!genusMap.contains(data.species.genus)) {
//                        familyMap[data.species.family]!!.add(data.species.genus)
//                        genusMap[data.species.genus] = arrayListOf()
//                        genusImages[data.species.genus] = arrayListOf()
//
//                        db.collection("genus").document(data.species.genus).set(
//                            hashMapOf(
//                                "name" to data.species.genus,
//                                "family" to data.species.family,
//                                "familyCommonName" to data.species.familyCommonName,
//                                "description" to "",
//                                "species" to arrayListOf<MutableMap<String, String>>(),
//                                "images" to arrayListOf<String>()
//                            )
//                        )
//                    }
//                    genusMap[data.species.genus]!!.add(data.species.scientificName to data.species.commonName)
//                    familyImages[data.species.family]!!.add(data.species.imageUrl)
//                    genusImages[data.species.genus]!!.add(data.species.imageUrl)
//                    db.collection("species").document(data.species.scientificName).set(
//                        hashMapOf(
//                            "commonName" to data.species.commonName,
//                            "scientificName" to data.species.scientificName,
//                            "genus" to data.species.genus,
//                            "commonFamilyName" to data.species.familyCommonName,
//                            "family" to data.species.family,
//                            "imageUrl" to data.species.imageUrl,
//                            "description" to "",
//                            "images" to hashMapOf(
//                                "leaf" to data.species.images.leaf,
//                                "fruit" to data.species.images.fruit,
//                                "flower" to data.species.images.flower,
//                                "bark" to data.species.images.bark,
//                                "habit" to data.species.images.habit
//                            )
//                        )
//                    )
                } catch (e: Exception) {
                }

            }
//            for (family in dataForDatabase){
//                db.collection("family").document(family.name).set(family)
//            }


//            for ((family, genusList) in familyMap) {
//                db.collection("families").document(family).update("genus", genusList)
//            }
//            for ((family, images) in familyImages) {
//                db.collection("families").document(family).update("images", images)
//            }
//
//            for ((genus, speciesList) in genusMap) {
//                db.collection("genus").document(genus).update("species", speciesList)
//            }
//            for ((genus, images) in genusImages) {
//                db.collection("genus").document(genus).update("images", images)
//            }
        }
    }

//    override fun onBackPressed() {}

    fun showDrawer() {
        drawer_layout.openDrawer(GravityCompat.START)
    }

    override fun onStart() {
        whatIsDendrologyTextView.setOnClickListener {
            findNavController(this, R.id.nav_host_fragment).navigate(R.id.whatIsDendrologyFragment)
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        taxonomyTreeTextView.setOnClickListener {
            findNavController(this, R.id.nav_host_fragment).navigate(R.id.taxonomyTreeFragment)
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        classificationTreeTextView.setOnClickListener {
            findNavController(this, R.id.nav_host_fragment).navigate(R.id.classificationTreeFragment)
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        namingTreesTextView.setOnClickListener {
            findNavController(this, R.id.nav_host_fragment).navigate(R.id.namingTreesFragment)
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        treeIdentificationTextView.setOnClickListener {
            findNavController(this, R.id.nav_host_fragment).navigate(R.id.treeIdentificationFragment)
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        urbanEnvironmentInfluenceTextView.setOnClickListener {
            findNavController(this, R.id.nav_host_fragment).navigate(R.id.urbanEnvironmentInfluenceFragment)
            drawer_layout.closeDrawer(GravityCompat.START)
        }

//        with(header) {
//            findViewById<View>(R.id.whatIsDendrologyTextView).setOnClickListener {
//                this@MainActivity.findNavController(
//                    R.id.nav_host_fragment
//                ).navigate(R.id.whatIsDendrologyFragment)
//            }
//            findViewById<View>(R.id.taxonomyTreeTextView).setOnClickListener {
//                findNavController(
//                    R.id.nav_host_fragment
//                ).navigate(R.id.taxonomyTreeFragment)
//            }
//            findViewById<View>(R.id.classificationTreeTextView).setOnClickListener {
//                findNavController(
//                    R.id.nav_host_fragment
//                ).navigate(R.id.classificationTreeFragment)
//            }
//            findViewById<View>(R.id.namingTreesTextView).setOnClickListener {
//                this@MainActivity.findNavController(
//                    R.id.nav_host_fragment
//                ).navigate(R.id.namingTreesFragment)
//            }
//            findViewById<View>(R.id.treeIdentificationTextView).setOnClickListener {
//                findNavController(
//                    R.id.nav_host_fragment
//                ).navigate(R.id.treeIdentificationFragment)
//            }
//            findViewById<View>(R.id.urbanEnvironmentInfluenceTextView).setOnClickListener {
//                findNavController(
//                    R.id.nav_host_fragment
//                ).navigate(R.id.urbanEnvironmentInfluenceFragment)
//            }
//        }
        super.onStart()
    }
}