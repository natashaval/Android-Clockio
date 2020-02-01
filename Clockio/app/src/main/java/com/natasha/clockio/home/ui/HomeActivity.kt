package com.natasha.clockio.home.ui

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.luseen.spacenavigation.SpaceItem
import com.luseen.spacenavigation.SpaceOnClickListener
import com.natasha.clockio.activity.ui.ActivityFragment
import com.natasha.clockio.home.ui.fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_home.*
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.natasha.clockio.R
import com.natasha.clockio.base.constant.FirebaseConst
import com.natasha.clockio.base.constant.ParcelableConst
import com.natasha.clockio.base.constant.PreferenceConst
import com.natasha.clockio.base.constant.UserConst
import com.natasha.clockio.home.ui.fragment.DashboardAdminFragment
import com.natasha.clockio.home.ui.fragment.FriendFragment
import com.natasha.clockio.home.ui.fragment.OnViewOpenedInterface
import com.natasha.clockio.notification.ui.NotifFragment
import com.natasha.clockio.presence.ui.PresenceActivity
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity(),
    OnViewOpenedInterface, ActivityFragment.OnEmployeeRefreshListener, NotifFragment.OnNotifReattachListener {

  companion object {
    private val TAG: String = HomeActivity::class.java.simpleName
    const val PRESENCE_ACTIVITY_REQUEST_CODE = 1
  }

  @Inject lateinit var sharedPref: SharedPreferences
  @Inject lateinit var firebaseInstance: FirebaseInstanceId
  @Inject lateinit var firebaseMessaging: FirebaseMessaging

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)

    AndroidInjection.inject(this)
    Log.d(TAG, "home onCreate")
    addSpaceNavigation(savedInstanceState)
    showFirstFragment()
    firebaseInstance()
  }

  override fun onBackPressed() {
    if (supportFragmentManager.backStackEntryCount > 1) {
      Log.d(TAG, "fragment backstack pop")
      supportFragmentManager.popBackStack()
    } else {
      Log.d(TAG, "fragment backpressed")
      super.onBackPressed()
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if(requestCode == PRESENCE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      val returnString = data?.getStringExtra(ParcelableConst.PRESENCE_FINISH)
      Log.d(TAG, "home onActivityResult $returnString")
      onEmployeeRefresh()
    }
  }

  fun addFragment(fragment: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .replace(R.id.content, fragment, fragment::class.java.simpleName)
        .commit()
  }

  fun addFragmentBackstack(fragment: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .replace(R.id.content, fragment, fragment::class.java.simpleName)
        .addToBackStack(null)
        .commit()
  }

  private fun addSpaceNavigation(savedInstanceState: Bundle?) {
    val userRole = sharedPref.getString(PreferenceConst.USER_ROLE_KEY, UserConst.ROLE_USER)
    spaceNavigation.initWithSaveInstanceState(savedInstanceState)
    if (userRole == UserConst.ROLE_ADMIN) spaceNavigation.addSpaceItem(SpaceItem(getString(R.string.navigation_dashboard), R.drawable.ic_speedometer))
    else spaceNavigation.addSpaceItem(SpaceItem(getString(R.string.navigation_activity), R.drawable.ic_dashboard_round))
    spaceNavigation.addSpaceItem(SpaceItem(getString(R.string.navigation_friend), R.drawable.ic_notebook_of_contacts))
    spaceNavigation.addSpaceItem(SpaceItem(getString(R.string.navigation_notif), R.drawable.ic_notifications_black_24dp))
    spaceNavigation.addSpaceItem(SpaceItem(getString(R.string.navigation_profile), R.drawable.ic_person_black_24dp))

    spaceNavigation.showIconOnly()
    spaceNavigation.setSpaceOnClickListener(mSpaceOnClickListener)
  }

  private val mSpaceOnClickListener = object : SpaceOnClickListener {
    override fun onCentreButtonClick() {
      Toast.makeText(applicationContext, "onCenterButtonClick", Toast.LENGTH_SHORT).show()
      val intent = Intent(this@HomeActivity, PresenceActivity::class.java)
      startActivityForResult(intent, PRESENCE_ACTIVITY_REQUEST_CODE)
    }

    override fun onItemClick(itemIndex: Int, itemName: String) {
//      Toast.makeText(applicationContext, "onItemClick $itemIndex $itemName", Toast.LENGTH_SHORT).show()
      when(itemName) {
        getString(R.string.navigation_dashboard) -> {
          val fragment = DashboardAdminFragment.newInstance()
          addFragment(fragment)
        }
        getString(R.string.navigation_activity) -> {
          val employeeId = sharedPref.getString(PreferenceConst.EMPLOYEE_ID_KEY, "")
          val fragment = ActivityFragment.newInstance(employeeId!!)
          addFragment(fragment)
        }
        getString(R.string.navigation_friend) -> {
          val fragment = FriendFragment.newInstance()
          addFragment(fragment)
        }
        getString(R.string.navigation_profile) -> {
          val fragment = ProfileFragment.newInstance()
          addFragment(fragment)
        }
        getString(R.string.navigation_notif) -> {
          val fragment = NotifFragment.newInstance()
          addFragment(fragment)
        }
      }
    }

    override fun onItemReselected(itemIndex: Int, itemName: String) {
//      Toast.makeText(applicationContext, "onReselected $itemIndex $itemName", Toast.LENGTH_SHORT).show()
    }
  }

  private fun showFirstFragment() {
    val userRole = sharedPref.getString(PreferenceConst.USER_ROLE_KEY, UserConst.ROLE_USER)
    val employeeId = sharedPref.getString(PreferenceConst.EMPLOYEE_ID_KEY, "")
    Log.d(TAG, "userRole $userRole")
    if (userRole == UserConst.ROLE_ADMIN) {
      val fragment = DashboardAdminFragment.newInstance()
      addFragment(fragment)
    } else {
      val fragment = ActivityFragment.newInstance(employeeId!!)
      addFragment(fragment)
    }
  }

  override fun onOpen() {
    Log.d(TAG, "space onOpen")
    spaceNavigation.visibility = View.GONE
    val contentMargin = content.layoutParams as ViewGroup.MarginLayoutParams
    contentMargin.bottomMargin = 0
  }

  override fun onClose() {
    Log.d(TAG, "space onClose")
    spaceNavigation.visibility = View.VISIBLE
    val contentMargin = content.layoutParams as ViewGroup.MarginLayoutParams
    contentMargin.bottomMargin = resources.getDimensionPixelOffset(R.dimen.space_navigation_height)
  }

  override fun onAttachFragment(fragment: Fragment) {
    when(fragment) {
      is ActivityFragment -> fragment.setOnEmployeeRefreshListener(this)
      is NotifFragment -> fragment.setOnNotifReattachListener(this)
    }
  }

  override fun onEmployeeRefresh() {
//    https://developer.android.com/training/basics/fragments/communicating.html
    val activityFrag = supportFragmentManager.
        findFragmentByTag(
            ActivityFragment::class.java.simpleName) as ActivityFragment
    Log.d(TAG, "home activity get Employee")
    activityFrag?.getEmployee()
  }

  override fun onNotifReattach() {
    val notifFrag = supportFragmentManager.findFragmentByTag(NotifFragment::class.java.simpleName) as NotifFragment
    Log.d(TAG, "home activity reattach notif")
    notifFrag.reAttachFragment()
  }

  private fun firebaseInstance() {
    firebaseInstance.instanceId
        .addOnCompleteListener(OnCompleteListener { task ->
          if (!task.isSuccessful) {
            Log.w(TAG, "getInstanceId failed", task.exception)
            return@OnCompleteListener
          }
          val token = task.result?.token
          Log.d(TAG, "firebase token $token")
        })

    //        https://firebase.google.com/docs/cloud-messaging/android/topic-messaging?authuser=0/
    firebaseMessaging.subscribeToTopic(FirebaseConst.topic)
        .addOnCompleteListener {task ->
          var msg = "Subscribe Topic Successful"
          if (!task.isSuccessful) {
            msg = "Subscribe Topic Failed"
          }
          Log.d(TAG, "firebase subscribe topic: $msg")
        }
  }

}
