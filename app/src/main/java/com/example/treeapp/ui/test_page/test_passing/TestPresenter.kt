package com.example.treeapp.ui.test_page.test_passing

import androidx.core.os.bundleOf
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.treeapp.Repository
import com.example.treeapp.entities.Species
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@InjectViewState
class TestPresenter(private val repository: Repository) : MvpPresenter<TestView>() {

    private var questionNumber = 0
    private var currentVariants = listOf<Species>()
    private var currentCorrectSpecies = Species()
    var currentHabitImage = ""
    var currentLeafImage = ""

    private val imagesHabitResult = hashMapOf<Int, String>()
    private val imagesLeafResult = hashMapOf<Int, String>()
    private val correctAnswers = hashMapOf<Int, String>()
    private val wrongAnswers = hashMapOf<Int, String>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        GlobalScope.launch {
            nextQuestion()
        }
    }

    suspend fun nextQuestion(answer: String = "") {
        when {
            questionNumber == 10 -> {
                Timber.d("Habit images: $imagesHabitResult")
                Timber.d("Leaf images: $imagesLeafResult")
                Timber.d("Correct answers: $correctAnswers")
                Timber.d("Wrong answers: $wrongAnswers")
                val bundle = bundleOf(
                    "habitImages" to imagesHabitResult,
                    "leafImages" to imagesLeafResult,
                    "correctAnswers" to correctAnswers,
                    "wrongAnswers" to wrongAnswers
                )
                viewState.goToResultScreen(bundle)
            }
            answer.isEmpty() -> {
                setNewData()
            }
            else -> {
                if (answer != currentCorrectSpecies.commonName && answer != currentCorrectSpecies.scientificName) {
                    for (species in currentVariants) {
                        if (species.commonName == answer || species.scientificName == answer) {
                            wrongAnswers[questionNumber] = answer
                        }
                    }
                }
                setNewData()
            }
        }
    }

    private suspend fun setNewData() {
        currentVariants = repository.getSpecies().shuffled().take(4)
        val fourSpeciesNames = mutableListOf<String>()
        for (species in currentVariants) {
            if (!species.commonName.isNullOrEmpty())
                fourSpeciesNames.add(species.commonName)
            else
                fourSpeciesNames.add(species.scientificName)
        }
        currentCorrectSpecies = currentVariants.shuffled().take(1)[0]
        val habitImages = mutableListOf(currentCorrectSpecies.imageUrl)
        if (currentCorrectSpecies.images.habit != null)
            for (habitImg in currentCorrectSpecies.images.habit!!)
                habitImages.add(habitImg)
        currentHabitImage = habitImages.shuffled().take(1)[0]
        currentLeafImage =
            currentCorrectSpecies.images.leaf?.shuffled()?.take(1)?.get(0) ?: ""

        if (currentHabitImage.isNullOrEmpty() || currentLeafImage.isNullOrEmpty()){
            setNewData()
            return
        }
        questionNumber += 1
        imagesHabitResult[questionNumber] = currentHabitImage
        imagesLeafResult[questionNumber] = currentLeafImage
        if (!currentCorrectSpecies.commonName.isNullOrEmpty())
            correctAnswers[questionNumber] = currentCorrectSpecies.commonName!!
        else
            correctAnswers[questionNumber] = currentCorrectSpecies.scientificName
        withContext(Dispatchers.Main) {
            viewState.displayData(
                currentHabitImage,
                fourSpeciesNames,
                questionNumber
            )
        }
    }
}