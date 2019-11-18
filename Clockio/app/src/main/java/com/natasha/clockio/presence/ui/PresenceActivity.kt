package com.natasha.clockio.presence.ui

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.natasha.clockio.R
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Looper
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.natasha.clockio.presence.ui.fragment.LockFragment
import com.nightonke.blurlockview.BlurLockView
import com.nightonke.blurlockview.Directions.HideType
import com.nightonke.blurlockview.Eases.EaseType
import com.nightonke.blurlockview.Password
import kotlinx.android.synthetic.main.activity_presence.*
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject

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
