package com.example.treeapp.di

import com.example.treeapp.Repository
import com.example.treeapp.ui.test_page.test_passing.TestPresenter
import com.example.treeapp.ui.test_page.test_result.TestResultPresenter
import org.koin.dsl.module

val appModule = module {
    factory { TestPresenter(get()) }
    factory { TestResultPresenter() }
    single { Repository() }
}