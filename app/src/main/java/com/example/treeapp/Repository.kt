package com.example.treeapp

import com.example.treeapp.entities.Species
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class Repository {

    private val db = Firebase.firestore
    private val speciesList = mutableListOf<Species>()

    suspend fun getSpecies(): MutableList<Species> {
        return if (speciesList.isNotEmpty())
            speciesList
        else {
            db.collection("species").get()
                .addOnSuccessListener { snapshot->
                    snapshot.forEach { doc ->
                        speciesList.add(doc.toObject())
                    }
                }.await()
            speciesList
        }

    }

}