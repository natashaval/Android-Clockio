package com.natasha.clockio.presence.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

import com.natasha.clockio.R
import com.natasha.clockio.base.model.BaseResponse
import com.natasha.clockio.location.LocationViewModel
import com.natasha.clockio.presence.service.request.CheckinRequest
import com.natasha.clockio.presence.service.response.EmotionRequest
import com.natasha.clockio.presence.service.response.EmotionResponse
import com.natasha.clockio.presence.viewModel.EmotionViewModel
import com.natasha.clockio.presence.viewModel.ImageViewModel
import com.natasha.clockio.presence.viewModel.PresenceViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_image.*
import kotlinx.android.synthetic.main.item_emotion.*
import java.io.File
import java.util.*
import javax.inject.Inject

class ImageFragment : Fragment() {

  companion object {
    fun newInstance() = ImageFragment()
    private val TAG = ImageFragment::class.java.simpleName
  }

  @Inject lateinit var sharedPref: SharedPreferences
  @Inject lateinit var factory: ViewModelProvider.Factory
  private lateinit var imageViewModel: ImageViewModel
  private lateinit var locationViewModel: LocationViewModel
  private lateinit var presenceViewModel: PresenceViewModel
  private lateinit var emotionViewModel: EmotionViewModel
  private lateinit var imagePath: String

  private var urlPhoto: String = ""

  /*private val apiEndPoint: String = "https://clockio-backend.cognitiveservices.azure.com/"
  private val subscriptionKey: String = "0491cab625b1413abb952f9487f0e30d"
  private val faceServiceClient = FaceServiceRestClient(apiEndPoint, subscriptionKey)*/

  override fun onAttach(context: Context) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_image, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    imageViewModel = ViewModelProvider(this, factory).get(ImageViewModel::class.java)
    locationViewModel = ViewModelProvider(this, factory).get(LocationViewModel::class.java)
    presenceViewModel = ViewModelProvider(this, factory).get(PresenceViewModel::class.java)
    emotionViewModel = ViewModelProvider(this, factory).get(EmotionViewModel::class.java)
    observeProgress()
    observeEvents()
//    observeCheckInResult()
    getEmotion()
    observeEmotion()
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
      activity!!.finish()
    }
  }

  //    https://cloudinary.com/documentation/android_image_and_video_upload
  //    https://cloudinary.com/documentation/image_upload_api_reference
  private fun sendImage(path: String) {
    imageCheck.setOnClickListener {
      imageViewModel.uploadImage(UUID.randomUUID().toString(), imagePath)
    }
  }

  private fun observeProgress(){
    imageViewModel.progressValue.observe(this, androidx.lifecycle.Observer {
      var prog = (it * 100).toInt()
//      Log.d(TAG, "progressImage $prog")
      progressImage.progress = prog
    })
  }

  private fun observeEvents(){
    imageViewModel.events.observe(this, androidx.lifecycle.Observer {
      when(it) {
        ImageViewModel.STATUS_CODE_STARTING -> {
          imageCheck.visibility = View.INVISIBLE
          imageClose.visibility = View.INVISIBLE
          Toast.makeText(activity, descriptionFromCode(it), Toast.LENGTH_SHORT).show()
        }
        ImageViewModel.STATUS_CODE_PROGRESS -> {
          progressImage.visibility = View.VISIBLE
        }
        ImageViewModel.STATUS_CODE_UPLOAD_ERROR-> {
          imageCheck.visibility = View.VISIBLE
          imageClose.visibility = View.VISIBLE
          progressImage.visibility = View.INVISIBLE
          Toast.makeText(activity, descriptionFromCode(it), Toast.LENGTH_SHORT).show()
        }
        ImageViewModel.STATUS_CODE_FINISHED -> {
          imageCheck.visibility = View.VISIBLE
          imageClose.visibility = View.VISIBLE
          progressImage.visibility = View.INVISIBLE
          Toast.makeText(activity, descriptionFromCode(it), Toast.LENGTH_SHORT).show()

          doCheckIn()
        }
      }
    })
  }

  private fun descriptionFromCode(status: Int): String {
    var resId: Int = R.string.cloudinary_default
    when(status) {
      ImageViewModel.STATUS_CODE_STARTING ->
        resId = R.string.cloudinary_start
      ImageViewModel.STATUS_CODE_UPLOAD_ERROR ->
        resId = R.string.cloudinary_error
      ImageViewModel.STATUS_CODE_FINISHED ->
        resId = R.string.cloudinary_success
    }
    return getString(resId)
  }

  private fun doCheckIn() {
    val employeeId = sharedPref.getString("id", null)
//    locationViewModel.getLocationData()
    imageViewModel.resultUrl.observe(this, androidx.lifecycle.Observer { url ->
      url?.let {
        Log.d(TAG, "url is exists $url")
        urlPhoto = url
//        var request = CheckinRequest(employeeId!!, url, Date(), 12.3, 45.6)
//        presenceViewModel.sendCheckIn(employeeId, request)
      }
    })
  }

  private fun observeCheckInResult() {
    presenceViewModel.presenceResult.observe(this, androidx.lifecycle.Observer {
      Log.d(TAG, "checkin in ImageFragment ${it.data}")
    })
  }

  private fun getEmotion() {
    uploadButton.setOnClickListener {
      emotionViewModel.getEmotion("gender,age,emotion", EmotionRequest(urlPhoto))
      progressImage.visibility = View.GONE
      imageCheck.visibility = View.GONE
      imageClose.visibility = View.GONE
      emotionLayout.visibility = View.VISIBLE
    }
  }

  private fun observeEmotion() {
    emotionViewModel.emotion.observe(this, androidx.lifecycle.Observer {
      when(it.status) {
        BaseResponse.Status.LOADING -> {
          emotionProgress.visibility = View.VISIBLE
        }
        BaseResponse.Status.SUCCESS -> {
          emotionProgress.visibility = View.GONE
          Log.d(TAG, "emotion success ${it.data}")
          it.data?.let {
            var dataList = it as List<EmotionResponse>
            var data = dataList[0]
            Log.d(TAG, "emotion satu $data")

            genderAge.text = getString(R.string.genderage, data.faceAttributes!!.gender, data.faceAttributes!!.age)
            anger.text = getString(R.string.anger, data.faceAttributes?.emotion?.anger)
            contempt.text = getString(R.string.contempt, data.faceAttributes?.emotion?.contempt)
            disgust.text = getString(R.string.disgust, data.faceAttributes?.emotion?.disgust)
            fear.text = getString(R.string.fear, data.faceAttributes?.emotion?.fear)
            happiness.text = getString(R.string.happiness, data.faceAttributes?.emotion?.happiness)
            neutral.text = getString(R.string.neutral, data.faceAttributes?.emotion?.neutral)
            sadness.text = getString(R.string.sadness, data.faceAttributes?.emotion?.sadness)
            surprise.text = getString(R.string.surprise, data.faceAttributes?.emotion?.surprise)

            var net = (data.faceAttributes?.emotion?.neutral)!!.times(100).toInt()
            Log.d(TAG, "emotion neutral Int $net")
            angerProgress.progress = (data.faceAttributes?.emotion?.anger)?.times(100)?.toInt()!!
            contemptProgress.progress = (data.faceAttributes?.emotion?.contempt)?.times(100)?.toInt()!!
            disgustProgress.progress = (data.faceAttributes?.emotion?.disgust)?.times(100)?.toInt()!!
            fearProgress.progress = (data.faceAttributes?.emotion?.fear)?.times(100)?.toInt()!!
            happinessProgress.progress = (data.faceAttributes?.emotion?.happiness)?.times(100)?.toInt()!!
            neutralProgress.progress = (data.faceAttributes?.emotion?.neutral)?.times(100)?.toInt()!!
            sadnessProgress.progress = (data.faceAttributes?.emotion?.sadness)?.times(100)?.toInt()!!
            surpriseProgress.progress = (data.faceAttributes?.emotion?.surprise)?.times(100)?.toInt()!!
          }
        }
        BaseResponse.Status.ERROR -> {
          emotionProgress.visibility = View.GONE
          Log.d(TAG, "emotion error ${it.data}")
        }
      }
    })
  }

  private fun setEmotionText(data: EmotionResponse) {
    Log.d(TAG, "emotion set text ${data.faceAttributes!!.gender}")
//    var haha = activity!!.resources.getString(R.string.genderage, data.faceAttributes!!.gender, data.faceAttributes!!.age)
//    Log.d(TAG, "emotion getString $haha")
//    genderAge.text = getString(R.string.genderage, data.faceAttributes!!.gender, data.faceAttributes!!.age)
//   anger.text = getString(R.string.anger, data.faceAttributes?.emotion?.anger)
//    contempt.text = getString(R.string.contempt, data.faceAttributes?.emotion?.contempt)
//    disgust.text = getString(R.string.disgust, data.faceAttributes?.emotion?.disgust)
//    fear.text = getString(R.string.fear, data.faceAttributes?.emotion?.fear)
//    happiness.text = getString(R.string.happiness, data.faceAttributes?.emotion?.happiness)
//    neutral.text = getString(R.string.neutral, data.faceAttributes?.emotion?.neutral)
//    sadness.text = getString(R.string.sadness, data.faceAttributes?.emotion?.sadness)
//    surprise.text = getString(R.string.surprise, data.faceAttributes?.emotion?.surprise)
  }

  private fun setEmotionProgressBar(data: EmotionResponse) {
    var net = (data.faceAttributes?.emotion?.neutral)!!.times(100).toInt()
    Log.d(TAG, "emotion neutral Int $net")
//    angerProgress.progress = (data.faceAttributes?.emotion?.anger)?.times(100)?.toInt()!!
//    contemptProgress.progress = (data.faceAttributes?.emotion?.contempt)?.times(100)?.toInt()!!
//    disgustProgress.progress = (data.faceAttributes?.emotion?.disgust)?.times(100)?.toInt()!!
//    fearProgress.progress = (data.faceAttributes?.emotion?.fear)?.times(100)?.toInt()!!
//    happinessProgress.progress = (data.faceAttributes?.emotion?.happiness)?.times(100)?.toInt()!!
//    neutralProgress.progress = (data.faceAttributes?.emotion?.neutral)?.times(100)?.toInt()!!
//    sadnessProgress.progress = (data.faceAttributes?.emotion?.sadness)?.times(100)?.toInt()!!
//    surpriseProgress.progress = (data.faceAttributes?.emotion?.surprise)?.times(100)?.toInt()!!
  }
}
