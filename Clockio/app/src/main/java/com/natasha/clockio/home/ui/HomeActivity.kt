package com.natasha.clockio.home.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.luseen.spacenavigation.SpaceItem
import com.luseen.spacenavigation.SpaceOnClickListener
import com.natasha.clockio.R
import com.natasha.clockio.home.ui.fragment.ActivityFragment
import com.natasha.clockio.home.ui.fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_home.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.natasha.clockio.home.ui.fragment.OnViewOpenedInterface
import com.natasha.clockio.presence.ui.PresenceActivity
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity


class HomeActivity : DaggerAppCompatActivity(), OnViewOpenedInterface {

    private val TAG: String = HomeActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        AndroidInjection.inject(this)
//        viewModel = ViewModelProvider(this, factory).get(ActivityViewModel::class.java)
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val fragment = ActivityFragment.newInstance()
        addFragment(fragment)
        addSpaceNavigation(savedInstanceState)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId) {
            R.id.navigation_activity -> {
                val fragment = ActivityFragment.newInstance()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                val fragment = ProfileFragment.newInstance()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content, fragment, fragment::class.java.simpleName)
            .commit()
    }

    private fun showDialog(dialogFragment: DialogFragment) {
        val ft = supportFragmentManager.beginTransaction()
        dialogFragment.show(ft, dialogFragment::class.java.simpleName)
    }

    private fun addSpaceNavigation(savedInstanceState: Bundle?) {
        spaceNavigation.initWithSaveInstanceState(savedInstanceState);
        spaceNavigation.addSpaceItem(SpaceItem(getString(R.string.navigation_activity), R.drawable.ic_dashboard_black_24dp))
        spaceNavigation.addSpaceItem(SpaceItem(getString(R.string.navigation_friend), R.drawable.ic_perm_contact_calendar_black_24dp))
        spaceNavigation.addSpaceItem(SpaceItem(getString(R.string.navigation_notification), R.drawable.ic_notifications_black_24dp))
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
//            Toast.makeText(applicationContext, "onItemClick $itemIndex $itemName", Toast.LENGTH_SHORT).show()
            when(itemName) {
                getString(R.string.navigation_activity) -> {
                    val fragment = ActivityFragment.newInstance()
                    addFragment(fragment)
                }
                getString(R.string.navigation_profile) -> {
                    val fragment = ProfileFragment.newInstance()
                    addFragment(fragment)
                }
                getString(R.string.navigation_friend) -> {
                    onOpen()
                }
                getString(R.string.navigation_notification) -> {
                    onClose()
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

}
