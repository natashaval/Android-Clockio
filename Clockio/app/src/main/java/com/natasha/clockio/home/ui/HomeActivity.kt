package com.natasha.clockio.home.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.luseen.spacenavigation.SpaceItem
import com.luseen.spacenavigation.SpaceOnClickListener
import com.natasha.clockio.home.ui.fragment.ActivityFragment
import com.natasha.clockio.home.ui.fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_home.*
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.natasha.clockio.R
import com.natasha.clockio.base.constant.FirebaseConst
import com.natasha.clockio.home.ui.fragment.FriendFragment
import com.natasha.clockio.home.ui.fragment.OnViewOpenedInterface
import com.natasha.clockio.notification.ui.NotifFragment
import com.natasha.clockio.presence.ui.PresenceActivity
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity(), OnViewOpenedInterface {

  private val TAG: String = HomeActivity::class.java.simpleName
  @Inject lateinit var firebaseInstance: FirebaseInstanceId
  @Inject lateinit var firebaseMessaging: FirebaseMessaging

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)

    AndroidInjection.inject(this)
    val fragment = ActivityFragment.newInstance()
    addFragment(fragment)
    addSpaceNavigation(savedInstanceState)
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
    spaceNavigation.initWithSaveInstanceState(savedInstanceState);
    spaceNavigation.addSpaceItem(SpaceItem(getString(R.string.navigation_activity), R.drawable.ic_dashboard_black_24dp))
    spaceNavigation.addSpaceItem(SpaceItem(getString(R.string.navigation_friend), R.drawable.ic_perm_contact_calendar_black_24dp))
    spaceNavigation.addSpaceItem(SpaceItem(getString(R.string.navigation_notif), R.drawable.ic_notifications_black_24dp))
    spaceNavigation.addSpaceItem(SpaceItem(getString(R.string.navigation_profile), R.drawable.ic_person_black_24dp))

    spaceNavigation.showIconOnly()
    spaceNavigation.setSpaceOnClickListener(mSpaceOnClickListener)
  }

  private val mSpaceOnClickListener = object : SpaceOnClickListener {
    override fun onCentreButtonClick() {
      Toast.makeText(applicationContext, "onCenterButtonClick", Toast.LENGTH_SHORT).show()
      val intent = Intent(this@HomeActivity, PresenceActivity::class.java)
      startActivity(intent)
    }

    override fun onItemClick(itemIndex: Int, itemName: String) {
      Toast.makeText(applicationContext, "onItemClick $itemIndex $itemName", Toast.LENGTH_SHORT).show()
      when(itemName) {
        getString(R.string.navigation_activity) -> {
          val fragment = ActivityFragment.newInstance()
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
      Toast.makeText(applicationContext, "onReselected $itemIndex $itemName", Toast.LENGTH_SHORT).show()
    }
  }

  override fun onOpen() {
    Log.d(TAG, "space onOpen")
    spaceNavigation.visibility = View.GONE
  }

  override fun onClose() {
    Log.d(TAG, "space onClose")
    spaceNavigation.visibility = View.VISIBLE
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
