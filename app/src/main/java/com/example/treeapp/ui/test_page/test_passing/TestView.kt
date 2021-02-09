package com.example.treeapp.ui.test_page.test_passing

import android.os.Bundle
import com.arellomobile.mvp.MvpView

interface TestView : MvpView {
    fun displayData(imageUrl: String?, variantsList: List<String>, questionNumber: Int)
    fun goToResultScreen(bundle: Bundle)
}