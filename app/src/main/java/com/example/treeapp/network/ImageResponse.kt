package com.example.treeapp.network

import com.google.gson.annotations.SerializedName

data class ImageResponse(
    val file: String,
    val remark: String,
    val timestamp: String
)
