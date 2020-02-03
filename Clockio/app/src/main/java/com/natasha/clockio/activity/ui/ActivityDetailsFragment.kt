package com.natasha.clockio.activity.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.natasha.clockio.R
import com.natasha.clockio.activity.viewmodel.ActivityViewModel
import com.natasha.clockio.base.constant.ParcelableConst
import com.natasha.clockio.base.ui.SweetAlertConfirmListener
import com.natasha.clockio.base.ui.alertConfirmDelete
import com.natasha.clockio.home.entity.Activity
import com.natasha.clockio.home.ui.fragment.OnViewOpenedInterface
import com.natasha.clockio.location.entity.LocationModel
import com.natasha.clockio.location.ui.MapsFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_activity_details.*
import kotlinx.android.synthetic.main.item_start_end_time.*
import javax.inject.Inject

class ActivityDetailsFragment : Fragment() {

  var act: Activity? = null
  @Inject lateinit var factory: ViewModelProvider.Factory
  private lateinit var viewModel: ActivityViewModel
  private lateinit var alertListener: SweetAlertConfirmListener

  companion object {
    fun newInstance() = ActivityDetailsFragment()
    val TAG: String = ActivityDetailsFragment::class.java.simpleName
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    arguments?.let {
      act = it.getParcelable(ParcelableConst.ITEM_ACTIVITY)
      Log.d(TAG, "details received $act")
    }
    setHasOptionsMenu(true)
    return inflater.inflate(R.layout.fragment_activity_details, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this, factory).get(ActivityViewModel::class.java)
    setDetails(act!!)
  }

  override fun onAttach(context: Context) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
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

//  https://stackoverflow.com/questions/18374183/how-to-show-icons-in-overflow-menu-in-actionbar
  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.update, menu)
    if (menu is MenuBuilder) {
      menu.setOptionalIconsVisible(true)
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId) {
      R.id.action_delete -> {
        act?.let {
          deleteActivity(it.id)
        }
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  private fun setDetails(act: Activity) {
    Log.d(TAG, "set in Activity $act")
    activityTitleDetails.text = act.title
    activityContentDetails.text = act.content.toString()
    startDetails.text = act.startTime.toString()
    endDetails.text = act.endTime.toString()
    activityDateDetails.text = act.date.toString()
    if (act.latitude != null && act.longitude != null) {
      setMap(LocationModel(act.latitude, act.longitude))
    }
  }

  private fun setMap(loc: LocationModel) {
    Log.d(TAG, "location details $loc")
    val frag = MapsFragment.newInstance()
    val bundle = Bundle().apply {
      putParcelable(ParcelableConst.LOCATION, loc)
      putBoolean(ParcelableConst.LOCATION_SAVE, false)
    }
    frag.arguments = bundle
    fragmentManager?.beginTransaction()
        ?.replace(R.id.activityMapDetails, frag, TAG)
        ?.commit()
  }

  private fun deleteActivity(id: String) {
    alertListener = object : SweetAlertConfirmListener {
      override fun onDelete(data: Any?) {
        val actId = data as String
        viewModel.deleteActivity(actId)
      }
    }
    alertConfirmDelete(activity!!, "Activity will be deleted!", alertListener, id)
  }
}
