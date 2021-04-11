package com.example.treeapp.util

import android.R.attr.bitmap
import android.graphics.Bitmap
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Core.*
import org.opencv.core.Mat
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc.*
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import timber.log.Timber


class ImageFormatter {

    fun processBitmap(image: Bitmap): Bitmap {

        OpenCVLoader.initDebug()

        if (OpenCVLoader.initDebug()) {
            val mat = Mat()
            val bmp32: Bitmap? = image.copy(Bitmap.Config.ARGB_8888, true)
            Utils.bitmapToMat(bmp32, mat)
//            inRange(mat, Scalar(30.0, 30.0, 30.0), Scalar(86.0, 255.0, 255.0), mat)
            inRange(mat, Scalar(0.0, 30.0, 0.0, 30.0), Scalar(255.0, 255.0, 100.0, 255.0), mat)
//            resize(mat, mat, Size(150.0, 150.0), 0.0, 0.0, INTER_NEAREST)
//            val newSrc = Mat(mat.size(), CV_MAKE_TYPE(source.depth(), 4)
//            normalize(mat, mat, 0.0, 1.0, NORM_MINMAX)
//            Timber.d("Mat size: ${mat.size()}")
//            Timber.d("Mat: $mat")
//            mat.reshape(150, 150, -1)
            val resultBitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(mat, resultBitmap)
            return resultBitmap
//            val x: Int = resultBitmap.width
//            val y: Int = resultBitmap.height
//            val intArray = Array<Array<>>(1)

//            bitmap.getPixels(intArray, 0, x, 0, 0, x, y)
//            return resultBitmap
//            Timber.d(mat.toString())
//            Creates inputs for reference.
//            val inputFeature0 = TensorBuffer.createFixedSize(
//                intArrayOf(1, 150, 150, 1),
//                DataType.FLOAT32
//            )
//            inputFeature0.loadBuffer(byteBuffer)
//
//// Runs model inference and gets result.
//            val outputs = model.process(inputFeature0)
//            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
//
//// Releases model resources if no longer used.
//            model.close()
//        } else {
//            Timber.d("OpenCVLoader Error")
//        }
//        val imageProcessor = ImageProcessor.Builder()
//            .add(NormalizeOp(0.5F, 0.5F))
//            .add(ResizeOp(150, 150, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
//            .build()
////
//        var tImage = TensorImage(DataType.FLOAT32)
////
//        tImage.load(resultBitmap)
//        tImage = imageProcessor.process(tImage)
//////        val array =
////            return resultBitmap
//            return tImage

        }
        return image
    }
}