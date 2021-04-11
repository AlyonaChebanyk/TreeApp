/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.treeapp.ui.recognition

import android.graphics.Bitmap
import android.graphics.Typeface
import android.media.ImageReader
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.util.Size
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.treeapp.R
import com.example.treeapp.Repository
import com.example.treeapp.util.BorderedText
import com.example.treeapp.util.Logger
import com.example.treeapp.util.ImageFormatter
import kotlinx.android.synthetic.main.tfe_ic_layout_bottom_sheet.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.tensorflow.lite.examples.classification.tflite.Classifier
import java.io.IOException

class ClassifierFragment : CameraFragment(), ImageReader.OnImageAvailableListener {
    private var rgbFrameBitmap: Bitmap? = null
    private var lastProcessingTimeMs: Long = 0
    private var sensorOrientation: Int? = null
    private var classifier: Classifier? = null
    private var borderedText: BorderedText? = null
    private val imageFormatter: ImageFormatter by inject()
    private val repository: Repository by inject()

    /** Input image size of the model along x axis.  */
    private var imageSizeX = 0

    /** Input image size of the model along y axis.  */
    private var imageSizeY = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        seeMoreTextView.setOnClickListener {
            val speciesName = detected_item.text.toString()
            speciesName.replace(" ", " ")
            GlobalScope.launch {
                val species = repository.getSpeciesByScientificName(speciesName)
                if (species == null)
                    Toast.makeText(activity, "No data for such plant", Toast.LENGTH_SHORT).show()
                else{
                    val bundle = bundleOf("species" to species)
                    findNavController().navigate(R.id.action_classifierFragment_to_speciesPageFragment, bundle)
                }
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.tfe_ic_camera_connection_fragment
    }

    override fun getDesiredPreviewFrameSize(): Size {
        return DESIRED_PREVIEW_SIZE
    }

    public override fun onPreviewSizeChosen(size: Size, rotation: Int) {
        val textSizePx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, resources.displayMetrics
        )
        borderedText = BorderedText(textSizePx)
        borderedText!!.setTypeface(Typeface.MONOSPACE)
        recreateClassifier(model, device, numThreads)
        if (classifier == null) {
            LOGGER.e("No classifier on preview!")
            return
        }
        previewWidth = size.width
        previewHeight = size.height
        sensorOrientation = rotation - screenOrientation
        LOGGER.i("Camera orientation relative to screen canvas: %d", sensorOrientation)
        LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight)
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888)
    }

    override fun processImage() {
        rgbFrameBitmap!!.setPixels(rgbBytes, 0, previewWidth, 0, 0, previewWidth, previewHeight)
        val cropSize = Math.min(previewWidth, previewHeight)
        rgbFrameBitmap = imageFormatter.processBitmap(rgbFrameBitmap!!)
        runInBackground {
            if (classifier != null) {
                val startTime = SystemClock.uptimeMillis()
                val results = classifier!!.recognizeImage(rgbFrameBitmap, sensorOrientation!!)
                lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime
                LOGGER.v("Detect: %s", results)
                requireActivity().runOnUiThread {
                    showResultsInBottomSheet(results)
//                    showFrameInfo(previewWidth.toString() + "x" + previewHeight)
//                    showCropInfo(imageSizeX.toString() + "x" + imageSizeY)
//                    showCameraResolution(cropSize.toString() + "x" + cropSize)
//                    showRotationInfo(sensorOrientation.toString())
//                    showInference(lastProcessingTimeMs.toString() + "ms")
                }
            }
            readyForNextImage()
        }
    }

    override fun onInferenceConfigurationChanged() {
        if (rgbFrameBitmap == null) {
            // Defer creation until we're getting camera frames.
            return
        }
        val device = device
        val model = model
        val numThreads = numThreads
        runInBackground { recreateClassifier(model, device, numThreads) }
    }

    private fun recreateClassifier(
        model: Classifier.Model,
        device: Classifier.Device,
        numThreads: Int
    ) {
        if (classifier != null) {
            LOGGER.d("Closing classifier.")
            classifier!!.close()
            classifier = null
        }
        if (device == Classifier.Device.GPU
            && (model == Classifier.Model.QUANTIZED_MOBILENET || model == Classifier.Model.QUANTIZED_EFFICIENTNET)
        ) {
            LOGGER.d("Not creating classifier: GPU doesn't support quantized models.")
            requireActivity().runOnUiThread {
                Toast.makeText(
                    activity,
                    R.string.tfe_ic_gpu_quant_error,
                    Toast.LENGTH_LONG
                ).show()
            }
            return
        }
        classifier = try {
            LOGGER.d(
                "Creating classifier (model=%s, device=%s, numThreads=%d)",
                model,
                device,
                numThreads
            )
            Classifier.create(
                activity, model, device, numThreads
            )
        } catch (e: IOException) {
            LOGGER.e(e, "Failed to create classifier.")
            requireActivity().runOnUiThread {
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
            }
            return
        } catch (e: IllegalArgumentException) {
            LOGGER.e(e, "Failed to create classifier.")
            requireActivity().runOnUiThread {
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
            }
            return
        }

        // Updates the input image size.
        imageSizeX = classifier!!.imageSizeX
        imageSizeY = classifier!!.imageSizeY
    }

    companion object {
        private val LOGGER = Logger()
        private val DESIRED_PREVIEW_SIZE = Size(640, 480)
        private const val TEXT_SIZE_DIP = 10f
    }
}