package com.example.treeapp.di

import com.example.treeapp.Repository
import com.example.treeapp.ui.atlas.family.FamilyListPresenter
import com.example.treeapp.ui.atlas.family.family_page.FamilyPagePresenter
import com.example.treeapp.ui.atlas.genus.GenusListPresenter
import com.example.treeapp.ui.atlas.genus.genus_page.GenusPagePresenter
import com.example.treeapp.ui.atlas.species.SpeciesListPresenter
import com.example.treeapp.ui.atlas.species.species_page.SpeciesPagePresenter
import com.example.treeapp.ui.recognition.RecognitionPresenter
import com.example.treeapp.ui.test_page.test_passing.TestPresenter
import com.example.treeapp.ui.test_page.test_result.TestResultPresenter
import com.example.treeapp.util.Logger
import org.koin.dsl.module

// инициализация модулей
val appModule = module {

    factory { TestPresenter(get()) }
    factory { TestResultPresenter() }
    factory { FamilyListPresenter(get()) }
    factory { GenusListPresenter(get()) }
    factory { SpeciesListPresenter(get()) }
    factory { FamilyPagePresenter() }
    factory { GenusPagePresenter() }
    factory { SpeciesPagePresenter() }
    factory { RecognitionPresenter(get(), get()) }

    single { Repository() }

    single { Logger() }
}