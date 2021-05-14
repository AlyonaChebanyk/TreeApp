package com.example.treeapp.network

import com.google.gson.annotations.SerializedName

data class SpeciesInfoResponse(
    @SerializedName("data")
    val species: Species
)

data class Species(
    @SerializedName("common_name")
    val commonName: String,
    @SerializedName("scientific_name")
    val scientificName: String,
    @SerializedName("family_common_name")
    val familyCommonName: String,
    @SerializedName("image_url")
    val imageUrl: String,
    val genus: String,
    val family: String,
    val images: ImageList
)

data class ImageList(
    val leaf: ArrayList<ImageUrl>,
    val fruit: ArrayList<ImageUrl>,
    val flower: ArrayList<ImageUrl>,
    val bark: ArrayList<ImageUrl>,
    val habit: ArrayList<ImageUrl>
)

data class ImageUrl(
    @SerializedName("image_url")
    val imageUrl: String
)
