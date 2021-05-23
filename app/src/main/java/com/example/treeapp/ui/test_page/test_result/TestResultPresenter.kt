package com.example.treeapp.ui.test_page.test_result

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

@InjectViewState
class TestResultPresenter : MvpPresenter<TestResultView>() {

    private lateinit var testResultAdapter: TestResultAdapter

    fun setData(
        habitImages: HashMap<Int, String>,
        leafImages: HashMap<Int, String>,
        correctAnswers: HashMap<Int, String>,
        wrongAnswers: HashMap<Int, String>
    ) {
        viewState.displayNumberOfCorrectAnswers(10 - wrongAnswers.size)
        val data = mutableListOf<TestResultItem>()
        for (i in 1..10){
            data.add(TestResultItem(habitImages[i], leafImages[i], correctAnswers[i]!!, wrongAnswers[i]))
            testResultAdapter = TestResultAdapter(data)
            viewState.setAdapter(testResultAdapter)
        }
    }
}