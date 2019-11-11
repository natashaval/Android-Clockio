package com.natasha.clockio.presence.ui

import android.app.Activity
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
import androidx.fragment.app.FragmentActivity
import com.nightonke.blurlockview.BlurLockView
import com.nightonke.blurlockview.Directions.HideType
import com.nightonke.blurlockview.Eases.EaseType
import com.nightonke.blurlockview.Password
import kotlinx.android.synthetic.main.activity_presence.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors


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
//        textView.setOnClickListener {
//            finish()
//        }

        val activity: FragmentActivity = this
        val executor = Executors.newSingleThreadExecutor()

//        https://developer.android.com/training/sign-in/biometric-auth
        val biometricManager = BiometricManager.from(this)
        when(biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d(TAG, "App can authenticate using biometrics")
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e(TAG, "No biometric features available on this device")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e(TAG, "Biometric features are currently unavailable")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Log.e(TAG, "The user hasn't associated any biometric credentials")
        }
        instanceOfBiometrics(activity, executor)
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

    //    https://android-developers.googleblog.com/2019/10/one-biometric-api-over-all-android.html
//    https://medium.com/mindorks/fingerprint-authentication-using-biometricprompt-compat-1466365b4795
    private fun instanceOfBiometrics(activity: FragmentActivity, executor: Executor) {
        val biometricPrompt = BiometricPrompt(activity, executor, object: BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.e(TAG, "Unrecoverable error has been encountered and operation is complete. $errString")
                activity.runOnUiThread {
                    Toast.makeText(activity, "Biometrics Error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    Log.e(TAG, "Negative Button")
                } else {
                    Log.e(TAG, "An unrecoverable error occured. $errString")
                }
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                activity.runOnUiThread {
                    Toast.makeText(activity, "Biometric success", Toast.LENGTH_SHORT).show()
                }
                Log.d(TAG, "Called when a biometric is recognized")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                activity.runOnUiThread {
                    Toast.makeText(activity, "Biometric Failed!", Toast.LENGTH_SHORT).show()
                }
                Log.d(TAG, "Called when a biometric is valid but not recognized")
            }
        })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Title")
            .setSubtitle("Subtitle to display")
            .setDescription("Set description")
            .setNegativeButtonText("cancel")
            .build()

        textView.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }
}
