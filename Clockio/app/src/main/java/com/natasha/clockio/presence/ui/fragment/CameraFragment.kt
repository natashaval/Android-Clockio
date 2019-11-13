package com.natasha.clockio.presence.ui.fragment

import android.content.pm.PackageManager
import android.graphics.Matrix
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Rational
import android.util.Size
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraX
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.natasha.clockio.R
import kotlinx.android.synthetic.main.camera_fragment.*
import java.util.concurrent.Executors

class CameraFragment : Fragment() {

  companion object {
    fun newInstance() = CameraFragment()

    private const val CAMERA_REQUEST_CODE_PERMISSIONS = 13
    private val CAMERA_REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
  }

  private lateinit var viewModel: CameraViewModel

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.camera_fragment, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(CameraViewModel::class.java)
    // TODO: Use the ViewModel
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    cameraTextView.setOnClickListener {
      Toast.makeText(activity, "Camera clicked!", Toast.LENGTH_SHORT).show()
      activity!!.finish()
    }

    viewFinder = activity!!.findViewById(R.id.cameraTexture)
    if (allPermissionGranted()) {
      viewFinder.post { startCamera() }
    } else {
      ActivityCompat.requestPermissions(activity!!, CAMERA_REQUIRED_PERMISSIONS, CAMERA_REQUEST_CODE_PERMISSIONS)
    }

    viewFinder.addOnLayoutChangeListener { view, i, i2, i3, i4, i5, i6, i7, i8 ->
      updateTransform()
    }
  }

  //    https://codelabs.developers.google.com/codelabs/camerax-getting-started/#4
  private val executor = Executors.newSingleThreadExecutor()
  private lateinit var viewFinder: TextureView

  private fun startCamera() {

    val preview = setupPreview()
    CameraX.bindToLifecycle(this, preview)
  }

  private fun setupPreview(): Preview {
    val previewConfig = PreviewConfig.Builder().apply {
      setLensFacing(CameraX.LensFacing.FRONT)
//      setTargetAspectRatio(AspectRatio.RATIO_4_3)
      setTargetResolution(Size(640, 480))
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

}
