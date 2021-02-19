package com.example.treeapp.entities

data class Family(
    val name: String = "",
    val commonName: String = "",
    val description: String = "",
    val genus: MutableList<String> = mutableListOf(),
    val images: MutableList<String> = mutableListOf()
)
