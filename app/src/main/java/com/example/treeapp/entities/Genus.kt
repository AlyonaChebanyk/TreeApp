package com.example.treeapp.entities

data class Genus(
    val name: String = "",
    val family: String = "",
    val familyCommonName: String = "",
    val description: String = "",
    val images: MutableList<String> = mutableListOf(),
    val species: MutableList<MutableMap<String, String>> = mutableListOf()
)