package com.example.treeapp.ui.recognition

import com.arellomobile.mvp.MvpView
import org.tensorflow.lite.examples.classification.tflite.Classifier

interface RecognitionView: MvpView {
    fun keepDeviceAwake()
    fun requestPermissionIfNeeded()
    fun setBottomSheetBehavior()
    fun showResultsInBottomSheet(results: List<Classifier.Recognition?>?)
    fun showToast(message: String)
    fun createClassifier()
    fun setFragment()
}