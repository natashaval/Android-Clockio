package com.natasha.clockio.presence.ui.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback

import com.natasha.clockio.R
import kotlinx.android.synthetic.main.image_fragment.*
import java.io.File
import java.util.*



class ImageFragment : Fragment() {

    companion object {
        fun newInstance() = ImageFragment()
        private val TAG = ImageFragment::class.java.simpleName
    }

    private lateinit var viewModel: ImageViewModel
    private lateinit var imagePath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.image_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ImageViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imagePath = arguments?.getString("imagePath", "").toString()
        Log.d(TAG, imagePath)
        Glide.with(this)
            .load(Uri.fromFile(File(imagePath)))
            .into(imageResult)

        closeImage()
        sendImage(imagePath)
    }

//    https://stackoverflow.com/questions/10863572/programmatically-go-back-to-the-previous-fragment-in-the-backstack
    private fun closeImage() {
        imageClose.setOnClickListener {
            /*if (activity!!.supportFragmentManager.backStackEntryCount > 0) {
                activity!!.supportFragmentManager.popBackStackImmediate()
            }
            else {
                Log.e(TAG, "Cannot close!")
            }*/
            activity!!.finish()
        }
    }

//    https://cloudinary.com/documentation/android_image_and_video_upload
//    https://cloudinary.com/documentation/image_upload_api_reference
    private fun sendImage(path: String) {
        MediaManager.init(context!!)
        imageCheck.setOnClickListener {
            MediaManager.get().upload(path)
                .unsigned("zmfwv7yt")
                .option("public_id", UUID.randomUUID().toString())
                .option("folder", "Android")
                .callback(object : UploadCallback {
                    override fun onSuccess(
                        requestId: String?,
                        resultData: MutableMap<Any?, Any?>?
                    ) {
                        Log.d(TAG, "Upload Cloudinary Success $requestId; result: $resultData")
//                        result: {resource_type=image, access_mode=public, etag=0a68980d44ad42d00ee15d0daba3b58f, signature=e9a53e383ca4ee45e7a35eb472e82844a3393ab5, url=http://res.cloudinary.com/jengsusy/image/upload/v1574009226/Android/0a87a882-4439-4501-9b1d-01282b374e4d.jpg, height=5173, secure_url=https://res.cloudinary.com/jengsusy/image/upload/v1574009226/Android/0a87a882-4439-4501-9b1d-01282b374e4d.jpg, existing=false, format=jpg, public_id=Android/0a87a882-4439-4501-9b1d-01282b374e4d, version=1574009226, original_filename=1574009168890, placeholder=false, width=3880, created_at=2019-11-17T16:47:06Z, tags=[], type=upload, bytes=3560202}
                    }

                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                        val progress = bytes / totalBytes
                        Log.d(TAG, "Upload Cloudinary Progress $progress")
                    }

                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {

                    }

                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        Log.e(TAG, "Upload Cloudinary Failed $requestId; error: $error")
                    }

                    override fun onStart(requestId: String?) {
                        Log.d(TAG, "Upload Cloudinary Started $requestId")
                    }

                })
                .dispatch()
        }
    }
}
