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

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.Camera
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.Image
import android.media.ImageReader
import android.os.*
import android.util.Size
import android.view.*
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.treeapp.R
import com.example.treeapp.util.BorderedText
import com.example.treeapp.util.ImageUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import kotlinx.android.synthetic.main.tfe_ic_activity_camera.*
import kotlinx.android.synthetic.main.tfe_ic_layout_bottom_sheet.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.tensorflow.lite.examples.classification.tflite.Classifier
import timber.log.Timber

class RecognitionFragment : MvpAppCompatFragment(), RecognitionView,
    ImageReader.OnImageAvailableListener,
    Camera.PreviewCallback {

    @InjectPresenter
    lateinit var presenter: RecognitionPresenter

    @ProvidePresenter
    fun provideRecognitionPresenter() = get<RecognitionPresenter>()

    private var sheetBehavior: BottomSheetBehavior<LinearLayout?>? = null
    private var useCamera2API = false
    private val PERMISSIONS_REQUEST = 1
    private val PERMISSION_CAMERA = Manifest.permission.CAMERA

    /** Input image size of the model along x axis.  */
    private var imageSizeX = 0

    /** Input image size of the model along y axis.  */
    private var imageSizeY = 0

    private var classifier: Classifier? = null
    private var borderedText: BorderedText? = null
    private val DESIRED_PREVIEW_SIZE = Size(640, 480)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        classifier = Classifier.create(requireActivity(), Classifier.Model.LEAF, Classifier.Device.CPU, 1)
        return inflater.inflate(R.layout.tfe_ic_activity_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seeMoreTextView.setOnClickListener {
            val speciesName = recognitionTextView.text.toString()
            speciesName.replace(" ", " ")
            GlobalScope.launch {
                val species = presenter.getSpeciesByScientificName(speciesName)
                if (species == null)
                    Toast.makeText(activity, "No data for such plant", Toast.LENGTH_SHORT).show()
                else {
                    val bundle = bundleOf("species" to species)
                    findNavController().navigate(
                        R.id.action_recognitionFragment_to_speciesPageFragment,
                        bundle
                    )
                }
            }
        }

//        requireActivity().setContentView(R.layout.tfe_ic_activity_camera)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST) {
            if (allPermissionsGranted(grantResults)) {
                setFragment()
            } else {
                requestPermission()
            }
        }
    }

    private fun allPermissionsGranted(grantResults: IntArray): Boolean {
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun hasPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().checkSelfPermission(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(PERMISSION_CAMERA)) {
                Toast.makeText(
                    this@RecognitionFragment.activity,
                    "Camera permission is required for this demo",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
            requestPermissions(arrayOf(PERMISSION_CAMERA), PERMISSIONS_REQUEST)
        }
    }

    // Returns true if the device supports the required hardware level, or better.
    private fun isHardwareLevelSupported(
        characteristics: CameraCharacteristics, requiredLevel: Int
    ): Boolean {
        val deviceLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)!!
        return if (deviceLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
            requiredLevel == deviceLevel
        } else requiredLevel <= deviceLevel
        // deviceLevel is not LEGACY, can use numerical sort
    }

    override fun showResultsInBottomSheet(results: List<Classifier.Recognition?>?) {
        if (results != null && results.size >= 3) {
            val recognition = results[0]
//            result.text = recognition?.title.toString()
            if (recognition != null) {
                if (recognition.title != null)
                    recognitionTextView.text = recognition.title
                if (recognition.confidence != null)
                    recognitionValueTextView.text =
                        String.format("%.2f", 100 * recognition.confidence) + "%"
            }
            val recognition1 = results[1]
            if (recognition1 != null) {
                if (recognition1.title != null)
                    recognition1TextView.text = recognition1.title
                if (recognition1.confidence != null)
                    recognition1ValueTextView.text =
                        String.format("%.2f", 100 * recognition1.confidence) + "%"
            }
            val recognition2 = results[2]
            if (recognition2 != null) {
                if (recognition2.title != null)
                    recognition2TextView.text = recognition2.title
                if (recognition2.confidence != null)
                    recognition2ValueTextView.text =
                        String.format("%.2f", 100 * recognition2.confidence) + "%"
            }
        }
    }

    private fun chooseCamera(): String? {
        val manager = requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            for (cameraId in manager.cameraIdList) {
                val characteristics = manager.getCameraCharacteristics(cameraId)

                // We don't use a front facing camera in this sample.
                val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue
                }
                val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    ?: continue

                // Fallback to camera1 API for internal cameras that don't have full support.
                // This should help with legacy situations where using the camera2 API causes
                // distorted or otherwise broken previews.
                useCamera2API = (facing == CameraCharacteristics.LENS_FACING_EXTERNAL
                        || isHardwareLevelSupported(
                    characteristics, CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL
                ))
                Timber.i("Camera API lv2?: %s", useCamera2API)
                return cameraId
            }
        } catch (e: CameraAccessException) {
            Timber.e(e, "Not allowed to access camera")
        }
        return null
    }

    override fun setFragment() {
        val cameraId = chooseCamera()
        val fragment: Fragment
        if (useCamera2API) {
            val camera2Fragment = CameraConnectionFragment.newInstance(
                { size, rotation ->
                    presenter.previewHeight = size.height
                    presenter.previewWidth = size.width
                    presenter.onPreviewSizeChosen(size, rotation, getScreenOrientation())
                },
                this,
                getLayoutId(),
                getDesiredPreviewFrameSize()
            )
            camera2Fragment.setCamera(cameraId)
            fragment = camera2Fragment
        } else {
            fragment = LegacyCameraConnectionFragment(
                this,
                getLayoutId(),
                getDesiredPreviewFrameSize()
            )
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    override fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    private fun getScreenOrientation()  =
        when (activity?.windowManager?.defaultDisplay?.rotation) {
            Surface.ROTATION_270 -> 270
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_90 -> 90
            else -> 0
        }

    private fun getLayoutId(): Int {
        return R.layout.tfe_ic_camera_connection_fragment
    }

    /** Callback for android.hardware.Camera API  */
    override fun onPreviewFrame(bytes: ByteArray, camera: Camera) {
        presenter.processImage(classifier!!, bytes, camera, getScreenOrientation())
    }

    /** Callback for Camera2 API  */
    override fun onImageAvailable(reader: ImageReader) {
        presenter.processImage(classifier!!, reader)
    }


    fun getDesiredPreviewFrameSize(): Size {
        return DESIRED_PREVIEW_SIZE
    }

    override fun keepDeviceAwake() {
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun requestPermissionIfNeeded() {
        if (hasPermission()) {
            setFragment()
        } else {
            requestPermission()
        }
    }

    override fun setBottomSheetBehavior() {
        sheetBehavior =
            BottomSheetBehavior.from(requireActivity().findViewById(R.id.bottom_sheet_layout))
        val vto = gestureLayout.viewTreeObserver
        vto.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        gestureLayout.viewTreeObserver.removeGlobalOnLayoutListener(this)
                    } else {
                        gestureLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                    //                int width = bottomSheetLayout.getMeasuredWidth();
                    val height = gestureLayout.measuredHeight
                    sheetBehavior!!.peekHeight = height
                }
            })
        sheetBehavior!!.isHideable = false
        sheetBehavior!!.setBottomSheetCallback(
            object : BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                        }
                        BottomSheetBehavior.STATE_DRAGGING -> {
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
    }

    override fun createClassifier() {
        classifier = Classifier.create(requireActivity(), Classifier.Model.LEAF, Classifier.Device.CPU, 1)

        // Updates the input image size.
        imageSizeX = classifier!!.imageSizeX
        imageSizeY = classifier!!.imageSizeY
    }
}