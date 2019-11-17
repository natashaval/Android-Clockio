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

import com.natasha.clockio.R
import kotlinx.android.synthetic.main.image_fragment.*
import java.io.File

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
}
