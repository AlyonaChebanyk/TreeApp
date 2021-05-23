package com.example.treeapp.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// класс вида
@Parcelize
data class Species(
    val commonName: String? = "",
    val scientificName: String = "",
    val genus: String = "",
    val commonFamilyName: String = "",
    val family: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val images: Images = Images()
): Parcelable

@Parcelize
data class Images(
    val habit: MutableList<String>? = mutableListOf(),
    val leaf: MutableList<String>? = mutableListOf(),
    val flower: MutableList<String>? = mutableListOf(),
    val fruit: MutableList<String>? = mutableListOf(),
    val bark: MutableList<String>? = mutableListOf()
): Parcelable

