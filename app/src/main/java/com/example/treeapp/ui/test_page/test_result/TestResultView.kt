package com.example.treeapp.ui.test_page.test_result

import com.arellomobile.mvp.MvpView

interface TestResultView: MvpView {
    fun setAdapter(testResultAdapter: TestResultAdapter)
}