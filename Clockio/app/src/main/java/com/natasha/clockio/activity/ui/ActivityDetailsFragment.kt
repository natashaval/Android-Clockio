package com.natasha.clockio.activity.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment

import com.natasha.clockio.R
import com.natasha.clockio.base.constant.ParcelableConst
import com.natasha.clockio.home.entity.Activity
import com.natasha.clockio.home.ui.fragment.OnViewOpenedInterface
import kotlinx.android.synthetic.main.fragment_activity_details.*

class ActivityDetailsFragment : Fragment() {

  var act: Activity? = null

  companion object {
    fun newInstance() = ActivityDetailsFragment()
    val TAG: String = ActivityDetailsFragment::class.java.simpleName
  }


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    if(arguments!=null){
      act = arguments!!.getParcelable(ParcelableConst.ITEM_ACTIVITY)
      Log.d(TAG, "details received $act")
    }
    return inflater.inflate(R.layout.fragment_activity_details, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    setDetails(act!!)
  }

  override fun onStart() {
    super.onStart()
    val i: OnViewOpenedInterface = activity as OnViewOpenedInterface
    i.onOpen()
  }

  override fun onStop() {
    super.onStop()
    val i: OnViewOpenedInterface = activity as OnViewOpenedInterface
    i.onClose()
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
  }

//  https://stackoverflow.com/questions/18374183/how-to-show-icons-in-overflow-menu-in-actionbar
  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.update, menu)
    if (menu is MenuBuilder) {
      menu.setOptionalIconsVisible(true)
    }
  }

  private fun setDetails(act: Activity) {
    Log.d(TAG, "set in Activity $act")
    activityTitleDetails.text = act.title
    activityContentDetails.text = act.content.toString()
    activityStartDetails.text = act.startTime.toString()
    activityEndDetails.text = act.endTime.toString()
  }
}
