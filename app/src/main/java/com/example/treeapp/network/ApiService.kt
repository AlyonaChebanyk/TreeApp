package com.example.treeapp.network

import com.example.treeapp.Constants
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {

    @Multipart
    @POST("file/upload/")
    suspend fun sentImageToServer(
        @Part image: MultipartBody.Part
    ): ImageResponse

    @GET("species/{species_name}")
    suspend fun getSpeciesInfo(
        @Path("species_name") speciesName: String,
        @Query("token") token: String = Constants.TREFLE_TOKEN
    ): SpeciesInfoResponse

}