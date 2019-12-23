package com.natasha.clockio.presence.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.natasha.clockio.R
import com.natasha.clockio.presence.ui.PresenceActivity
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.File
import java.lang.Exception
import java.util.concurrent.Executors

class CameraFragment : Fragment() {

  companion object {
    fun newInstance() = CameraFragment()

    private const val CAMERA_REQUEST_CODE_PERMISSIONS = 12
    private val CAMERA_REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val TAG = CameraFragment::class.java.simpleName
  }

  private var lensFacing = CameraX.LensFacing.FRONT
  private val screenAspectRatio = AspectRatio.RATIO_4_3
  private lateinit var outputDirectory: File

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_camera, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
//    cameraTextView.setOnClickListener {
//      Toast.makeText(activity, "Camera clicked!", Toast.LENGTH_SHORT).show()
//      activity!!.finish()
//    }

    outputDirectory = PresenceActivity.getOutputDirectory(requireContext())
    viewFinder = activity!!.findViewById(R.id.cameraTexture)
    if (allPermissionGranted()) {
      viewFinder.post { startCamera() }
    } else {
      ActivityCompat.requestPermissions(activity!!, CAMERA_REQUIRED_PERMISSIONS, CAMERA_REQUEST_CODE_PERMISSIONS)
    }

    viewFinder.addOnLayoutChangeListener { view, i, i2, i3, i4, i5, i6, i7, i8 ->
      updateTransform()
    }
    switchCamera()
  }

  //    https://codelabs.developers.google.com/codelabs/camerax-getting-started/#4
  private val executor = Executors.newSingleThreadExecutor()
  private lateinit var viewFinder: TextureView

  private fun startCamera() {

    val preview = setupPreview()
    // Setup image capture
    val imageCapture = setupImageCapture()
    CameraX.bindToLifecycle(this, preview, imageCapture)
  }

  private fun setupPreview(): Preview {
    val previewConfig = PreviewConfig.Builder().apply {
      setLensFacing(lensFacing)
      setTargetAspectRatio(screenAspectRatio)
//      setTargetResolution(Size(640, 480))
      setTargetRotation(viewFinder.display.rotation)
    }.build()

    val preview = Preview(previewConfig)
    preview.setOnPreviewOutputUpdateListener {
      val parent = viewFinder.parent as ViewGroup
      parent.removeView(viewFinder)
      parent.addView(viewFinder, 0)

      viewFinder.surfaceTexture = it.surfaceTexture
      updateTransform()
    }
    return preview
  }

  private fun setupImageCapture(): ImageCapture {
    val imageCaptureConfig = ImageCaptureConfig.Builder().apply {
      setLensFacing(lensFacing)
      setTargetAspectRatio(screenAspectRatio)
      setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
      setTargetRotation(viewFinder.display.rotation)
    }.build()

    val imageCapture = ImageCapture(imageCaptureConfig)
    cameraCaptureButton.setOnClickListener {
      Log.d(TAG, "Camera Capture Clicked!")
      val file = File(outputDirectory, "${System.currentTimeMillis()}.jpg")
      imageCapture.takePicture(file, executor,
        object: ImageCapture.OnImageSavedListener {
          override fun onImageSaved(file: File) {
            val msg = "Photo capture succeeded: ${file.absolutePath}"
            Log.d(TAG, msg)
            viewFinder.post {
              Toast.makeText(activity!!, msg, Toast.LENGTH_SHORT).show()
              sendImagePathToFragment(file.absolutePath)
            }
          }

          override fun onError(
            imageCaptureError: ImageCapture.ImageCaptureError,
            message: String,
            cause: Throwable?
          ) {
            val msg = "Photo capture failed: $message"
            Log.e(TAG, msg, cause)
            cause?.printStackTrace()
            viewFinder.post {
              Toast.makeText(activity!!, msg, Toast.LENGTH_SHORT).show()
            }
          }

        })
    }
    return imageCapture
  }

  @SuppressLint("RestrictedApi")
  private fun switchCamera() {
    cameraSwitchButton.setOnClickListener {
      lensFacing = if (lensFacing == CameraX.LensFacing.FRONT) {
        CameraX.LensFacing.BACK
      } else {
        CameraX.LensFacing.FRONT
      }

      try {
        CameraX.getCameraWithLensFacing(lensFacing)
        CameraX.unbindAll()
        startCamera()

      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  private fun updateTransform() {
    val matrix = Matrix()
    val centerX = viewFinder.width / 2f
    val centerY = viewFinder.height / 2f

    val rotationDegrees = when(viewFinder.display.rotation) {
      Surface.ROTATION_0 -> 0
      Surface.ROTATION_90 -> 90
      Surface.ROTATION_180 -> 180
      Surface.ROTATION_270 -> 270
      else -> return
    }

    matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)
    viewFinder.setTransform(matrix)
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    if (requestCode == CAMERA_REQUEST_CODE_PERMISSIONS) {
      if (allPermissionGranted()) {
        viewFinder.post { startCamera() }
      } else {
        Toast.makeText(activity, getString(R.string.camera_permission), Toast.LENGTH_SHORT).show()
        activity!!.finish()
      }
    }
  }

  private fun allPermissionGranted() = CAMERA_REQUIRED_PERMISSIONS.all {
    ContextCompat.checkSelfPermission(activity!!.baseContext, it) == PackageManager.PERMISSION_GRANTED
  }

//  https://stackoverflow.com/questions/34847634/passing-image-from-one-fragment-to-a-another-fragment-and-display-image-in-that
  private fun sendImagePathToFragment(path: String) {
    val frag = ImageFragment.newInstance()
    var bundle = Bundle().apply {
      putString("imagePath", path)
    }
    frag.arguments = bundle

  activity!!.supportFragmentManager.beginTransaction()
    .replace(R.id.presenceContent, frag, frag::class.java.simpleName)
    .addToBackStack(null)
    .commit()
  }

}
