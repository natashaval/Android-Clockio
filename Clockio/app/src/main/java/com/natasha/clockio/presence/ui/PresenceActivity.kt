package com.natasha.clockio.presence.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.natasha.clockio.R
import androidx.fragment.app.Fragment
import com.natasha.clockio.presence.ui.fragment.LockFragment
import java.io.File

class PresenceActivity : AppCompatActivity() {

    private val TAG: String? = PresenceActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        https://github.com/Nightonke/BlurLockView/blob/master/app/src/main/java/com/nightonke/blurlockviewsample/ShowActivity.java
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_presence)

        val fragment = LockFragment.newInstance()
        addFragment(fragment)

    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.presenceContent, fragment, fragment::class.java.simpleName)
            .commit()
    }

    companion object {
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
            }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }
    }
}
