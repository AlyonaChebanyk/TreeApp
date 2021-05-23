package com.example.treeapp.entities

data class Family2 (
    val name: String = "",
    val commonName: String = "",
    val description: String = "",
    val genus: MutableList<Genus2> = mutableListOf()
)