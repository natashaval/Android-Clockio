package com.natasha.clockio.home.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.luseen.spacenavigation.SpaceItem
import com.luseen.spacenavigation.SpaceNavigationView
import com.luseen.spacenavigation.SpaceOnClickListener
import com.natasha.clockio.R
import com.natasha.clockio.home.ui.fragment.ActivityFragment
import com.natasha.clockio.home.ui.fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_home.*
import android.widget.Toast
import com.natasha.clockio.MainActivity
import com.natasha.clockio.presence.ui.PresenceActivity


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
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

    private fun addSpaceNavigation(savedInstanceState: Bundle?) {
        spaceNavigation.initWithSaveInstanceState(savedInstanceState);
        spaceNavigation.addSpaceItem(SpaceItem("Home", R.drawable.ic_dashboard_black_24dp))
        spaceNavigation.addSpaceItem(SpaceItem("Contact", R.drawable.ic_perm_contact_calendar_black_24dp))
        spaceNavigation.addSpaceItem(SpaceItem("Notif", R.drawable.ic_notifications_black_24dp))
        spaceNavigation.addSpaceItem(SpaceItem("Profile", R.drawable.ic_person_black_24dp))

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
                "Home" -> {
                    val fragment = ActivityFragment.newInstance()
                    addFragment(fragment)
                }
                "Profile" -> {
                    val fragment = ProfileFragment.newInstance()
                    addFragment(fragment)
                }
            }
        }

        override fun onItemReselected(itemIndex: Int, itemName: String) {
            Toast.makeText(applicationContext, "onReselected $itemIndex $itemName", Toast.LENGTH_SHORT).show()
        }
    }
}
