package com.example.treeapp.ui.recognition

import android.graphics.Bitmap
import android.graphics.Typeface
import android.hardware.Camera
import android.hardware.camera2.CameraCharacteristics
import android.media.Image
import android.media.ImageReader
import android.os.SystemClock
import android.os.Trace
import android.util.Size
import android.util.TypedValue
import android.view.Surface
import androidx.fragment.app.Fragment
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.treeapp.R
import com.example.treeapp.Repository
import com.example.treeapp.util.BorderedText
import com.example.treeapp.util.ImageUtils
import com.example.treeapp.util.Logger
import kotlinx.android.synthetic.main.tfe_ic_layout_bottom_sheet.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.examples.classification.tflite.Classifier
import timber.log.Timber
import java.io.IOException
import kotlin.math.min

@InjectViewState
class RecognitionPresenter(private val repository: Repository, private val logger: Logger) :
    MvpPresenter<RecognitionView>() {

    private var isProcessingFrame = false
    private val yuvBytes = arrayOfNulls<ByteArray>(3)
    private var rgbBytes: IntArray? = null
    private var luminanceStride = 0
    private var postInferenceCallback: Runnable? = null
    private var imageConverter: Runnable? = null
    private val model = Classifier.Model.LEAF
    private val device = Classifier.Device.CPU
    var previewWidth = 0
    var previewHeight = 0

    private var numThreads = -1

    private var rgbFrameBitmap: Bitmap? = null
    private var lastProcessingTimeMs: Long = 0
    private var sensorOrientation: Int? = null

    suspend fun getSpeciesByScientificName(name: String) =
        repository.getSpeciesByScientificName(name)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.keepDeviceAwake()
        viewState.requestPermissionIfNeeded()
        viewState.setBottomSheetBehavior()
    }

    fun onPreviewSizeChosen(size: Size, rotation: Int, screenOrientation: Int) {
        viewState.createClassifier()
        previewWidth = size.width
        previewHeight = size.height
        sensorOrientation = rotation - screenOrientation
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888)
    }

    fun processImage(classifier: Classifier, bytes: ByteArray, camera: Camera, screenOrientation: Int){
        if (isProcessingFrame) {
//            logger.w("Dropping frame!")
            return
        }
        try {
            // Initialize the storage bitmaps once when the resolution is known.
            if (rgbBytes == null) {
                val previewSize = camera.parameters.previewSize
                previewHeight = previewSize.height
                previewWidth = previewSize.width
                rgbBytes = IntArray(previewWidth * previewHeight)
                onPreviewSizeChosen(Size(previewSize.width, previewSize.height), 90, screenOrientation)
            }
        } catch (e: Exception) {
//            logger.e(e, "Exception!")
            return
        }
        isProcessingFrame = true
        yuvBytes[0] = bytes
        luminanceStride = previewWidth
        imageConverter = Runnable {
            ImageUtils.convertYUV420SPToARGB8888(
                bytes,
                previewWidth,
                previewHeight,
                rgbBytes
            )
        }
        postInferenceCallback = Runnable {
            camera.addCallbackBuffer(bytes)
            isProcessingFrame = false
        }
        imageConverter!!.run()
        rgbFrameBitmap!!.setPixels(
            rgbBytes,
            0,
            previewWidth,
            0,
            0,
            previewWidth,
            previewHeight
        )
        GlobalScope.launch {
            val results = classifier.recognizeImage(rgbFrameBitmap, sensorOrientation!!)
            logger.v("Detect: %s", results)
            withContext(Dispatchers.Main) {
                viewState.showResultsInBottomSheet(results)
            }
            readyForNextImage()
        }
    }

    fun processImage(classifier: Classifier, reader: ImageReader){
        // We need wait until we have some size from onPreviewSizeChosen
        if (previewWidth == 0 || previewHeight == 0) {
            return
        }
        if (rgbBytes == null) {
            rgbBytes = IntArray(previewWidth * previewHeight)
        }
        try {
            val image = reader.acquireLatestImage() ?: return
            if (isProcessingFrame) {
                image.close()
                return
            }
            isProcessingFrame = true
            Trace.beginSection("imageAvailable")
            val planes = image.planes
            fillBytes(planes, yuvBytes)
            luminanceStride = planes[0].rowStride
            val uvRowStride = planes[1].rowStride
            val uvPixelStride = planes[1].pixelStride
            imageConverter = object : Runnable {
                override fun run() {
                    ImageUtils.convertYUV420ToARGB8888(
                        yuvBytes[0],
                        yuvBytes[1],
                        yuvBytes[2],
                        previewWidth,
                        previewHeight,
                        luminanceStride,
                        uvRowStride,
                        uvPixelStride,
                        rgbBytes
                    )
                }
            }
            postInferenceCallback = Runnable {
                image.close()
                isProcessingFrame = false
            }
            imageConverter!!.run()
            rgbFrameBitmap!!.setPixels(
                rgbBytes,
                0,
                previewWidth,
                0,
                0,
                previewWidth,
                previewHeight
            )
            GlobalScope.launch {
                val results = classifier.recognizeImage(rgbFrameBitmap, sensorOrientation!!)
                logger.v("Detect: %s", results)
                withContext(Dispatchers.Main) {
                    viewState.showResultsInBottomSheet(results)
                }
                readyForNextImage()
            }
        } catch (e: Exception) {
//            logger.e(e, "Exception!")
            Trace.endSection()
            return
        }
        Trace.endSection()

    }

    private fun readyForNextImage() {
        if (postInferenceCallback != null) {
            postInferenceCallback!!.run()
        }
    }

    private fun fillBytes(planes: Array<Image.Plane>, yuvBytes: Array<ByteArray?>) {
        // Because of the variable row stride it's not possible to know in
        // advance the actual necessary dimensions of the yuv planes.
        for (i in planes.indices) {
            val buffer = planes[i].buffer
            if (yuvBytes[i] == null) {
//                logger.d(
//                    "Initializing buffer %d at size %d",
//                    i,
//                    buffer.capacity()
//                )
                yuvBytes[i] = ByteArray(buffer.capacity())
            }
            buffer[yuvBytes[i]]
        }
    }

}
