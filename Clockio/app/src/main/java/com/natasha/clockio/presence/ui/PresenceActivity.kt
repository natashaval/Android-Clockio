package com.natasha.clockio.presence.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.natasha.clockio.R
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.nightonke.blurlockview.BlurLockView
import com.nightonke.blurlockview.Directions.HideType
import com.nightonke.blurlockview.Eases.EaseType
import com.nightonke.blurlockview.Password
import kotlinx.android.synthetic.main.activity_presence.*


class PresenceActivity : AppCompatActivity() {

    private val TAG: String? = PresenceActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        https://github.com/Nightonke/BlurLockView/blob/master/app/src/main/java/com/nightonke/blurlockviewsample/ShowActivity.java
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_presence)
        setLock()
        textView.setOnClickListener {
            finish()
        }
    }

    private fun setLock() {
        blurLockView.setBlurredView(imageView1)
        blurLockView.setTitle("This is Title")
        blurLockView.setLeftButton("Left Btn")
        blurLockView.setRightButton("Right Btn")
        blurLockView.setCorrectPassword(password.text.toString())

        blurLockView.setType(Password.NUMBER, false)
        blurLockView.setOnLeftButtonClickListener(mOnLeftButtonClickListener)
        blurLockView.setOnPasswordInputListener(mOnPasswordInputListener)
    }

    private val mOnPasswordInputListener = object : BlurLockView.OnPasswordInputListener {
        override fun correct(inputPassword: String?) {
            Toast.makeText(applicationContext, "Correct", Toast.LENGTH_SHORT).show()
            blurLockView.hide(1000, HideType.FADE_OUT, EaseType.EaseInBack);
        }

        override fun incorrect(inputPassword: String?) {
            Toast.makeText(applicationContext, "Password incorrect", Toast.LENGTH_SHORT).show()
        }

        override fun input(inputPassword: String?) {
            Log.d(TAG, "input: $inputPassword")
        }

    }

    private val mOnLeftButtonClickListener = object: BlurLockView.OnLeftButtonClickListener {
        override fun onClick() {
            Toast.makeText(applicationContext, "Left Button clicked", Toast.LENGTH_SHORT).show()
        }

    }
}
