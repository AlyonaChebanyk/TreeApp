package com.example.treeapp

import com.example.treeapp.entities.Family
import com.example.treeapp.entities.Genus
import com.example.treeapp.entities.Species
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class Repository {

    private val db = Firebase.firestore
    private val speciesList = mutableListOf<Species>()
    private val familyList = mutableListOf<Family>()
    private val genusList = mutableListOf<Genus>()

    suspend fun getSpecies(): MutableList<Species> {
        return if (speciesList.isNotEmpty())
            speciesList
        else {
            db.collection("species").get()
                .addOnSuccessListener { snapshot ->
                    snapshot.forEach { doc ->
                        speciesList.add(doc.toObject())
                    }
                }.await()
            speciesList
        }

    }

    suspend fun getFamilies(): MutableList<Family> {
        return if (familyList.isNotEmpty())
            familyList
        else{
            db.collection("families").get()
                .addOnSuccessListener { snapshot ->
                    snapshot.forEach { doc ->
                        familyList.add(doc.toObject())
                    }
                }.await()
            familyList
        }
    }

    suspend fun getGenusList(): MutableList<Genus> {
        return if (genusList.isNotEmpty())
            genusList
        else{
            db.collection("genus").get()
                .addOnSuccessListener { snapshot ->
                    snapshot.forEach { doc ->
                        genusList.add(doc.toObject())
                    }
                }.await()
            genusList
        }
    }
}