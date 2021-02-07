package com.example.treeapp.di

import com.example.treeapp.Constants
import com.example.treeapp.network.ApiService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { provideRetrofit(Constants.BASE_URL) }
    single { provideApiService(get()) }
}
//"https://tree-app123.herokuapp.com/"
fun provideRetrofit(url: String): Retrofit = Retrofit.Builder()
    .baseUrl(url)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

fun provideApiService(retrofit: Retrofit): ApiService =
    retrofit.create(ApiService::class.java)
