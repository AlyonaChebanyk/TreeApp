package com.example.treeapp.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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
    val habit: MutableList<ImageUrl>? = mutableListOf(),
    val leaf: MutableList<ImageUrl>? = mutableListOf(),
    val flower: MutableList<ImageUrl>? = mutableListOf(),
    val fruit: MutableList<ImageUrl>? = mutableListOf(),
    val bark: MutableList<ImageUrl>? = mutableListOf()
): Parcelable

@Parcelize
data class ImageUrl(
    val imageUrl: String = ""
): Parcelable
