package com.example.treeapp.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Genus2(
    val name: String = "",
    val description: String = "",
    val species: MutableList<Species2> = mutableListOf()
): Parcelable