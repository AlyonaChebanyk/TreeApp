package com.example.treeapp.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// класс семейства
@Parcelize
data class Family(
    val name: String = "",
    val commonName: String = "",
    val description: String = "",
    val genus: MutableList<String> = mutableListOf(),
    val images: MutableList<String> = mutableListOf()
): Parcelable
