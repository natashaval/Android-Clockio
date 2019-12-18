package com.natasha.clockio.presence.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity

import com.nightonke.blurlockview.BlurLockView
import com.nightonke.blurlockview.Directions.HideType
import com.nightonke.blurlockview.Eases.EaseType
import com.nightonke.blurlockview.Password
import java.util.concurrent.Executor
import androidx.biometric.BiometricManager
import androidx.fragment.app.Fragment
import com.natasha.clockio.R
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.lock_fragment.*
import java.util.concurrent.Executors
import javax.inject.Inject


class LockFragment : Fragment() {

    private val TAG: String? = LockFragment::class.java.simpleName
    @Inject lateinit var sharedPref: SharedPreferences

    companion object {
        fun newInstance() = LockFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreate(savedInstanceState)
//        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
        return inflater.inflate(R.layout.lock_fragment, container, false)
    }

//    override fun onStart() {
//        super.onStart()
//        val d: Dialog = dialog!!
//        d.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    //        https://stackoverflow.com/questions/33469159/android-fragment-and-null-object-reference
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        helloPresence.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.presenceContent, CameraFragment.newInstance())
                .commit()
        }


        val activity: FragmentActivity? = activity
        val executor = Executors.newSingleThreadExecutor()

//        https://developer.android.com/training/sign-in/biometric-auth
        val biometricManager = BiometricManager.from(context!!)
        when(biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d(TAG, "App can authenticate using biometrics")
                instanceOfBiometrics(activity!!, executor)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e(TAG, "No biometric features available on this device")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e(TAG, "Biometric features are currently unavailable")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Log.e(TAG, "The user hasn't associated any biometric credentials")
        }

        setLock()
    }

//    https://developer.android.com/guide/topics/ui/dialogs.html#FullscreenDialog
//    https://medium.com/alexander-schaefer/implementing-the-new-material-design-full-screen-dialog-for-android-e9dcc712cb38
    /*override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }*/

    private fun setLock() {
        blurLockView.setBlurredView(imageView1)
        blurLockView.setTitle(getString(R.string.biometric_title))
        blurLockView.setLeftButton(getString(R.string.blurlock_left_button))
        blurLockView.setRightButton(getString(R.string.blurlock_right_button))
        blurLockView.setCorrectPassword(sharedPref.getString(
            getString(R.string.settings_pin_key), getString(R.string.settings_pin_default_value)))

        blurLockView.setType(Password.NUMBER, false)
        blurLockView.setOnPasswordInputListener(mOnPasswordInputListener)
    }

    private val mOnPasswordInputListener = object : BlurLockView.OnPasswordInputListener {
        override fun correct(inputPassword: String?) {
            Toast.makeText(activity, "Correct", Toast.LENGTH_SHORT).show()
            blurLockView.hide(1000, HideType.FADE_OUT, EaseType.EaseInBack);
        }

        override fun incorrect(inputPassword: String?) {
            Toast.makeText(activity, "Password incorrect", Toast.LENGTH_SHORT).show()
        }

        override fun input(inputPassword: String?) {
            Log.d(TAG, "input: $inputPassword")
        }

    }

    private val mOnLeftButtonClickListener = BlurLockView.OnLeftButtonClickListener {
        Toast.makeText(activity, "Left Button clicked", Toast.LENGTH_SHORT).show()
    }

    //    https://android-developers.googleblog.com/2019/10/one-biometric-api-over-all-android.html
//    https://medium.com/mindorks/fingerprint-authentication-using-biometricprompt-compat-1466365b4795
    private fun instanceOfBiometrics(activity: FragmentActivity?, executor: Executor) {
        val biometricPrompt = BiometricPrompt(this@LockFragment, executor, object: BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.e(TAG, "Unrecoverable error has been encountered and operation is complete. $errString")
                activity!!.runOnUiThread {
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
                activity!!.runOnUiThread {
                    Toast.makeText(activity, getString(R.string.biometric_success), Toast.LENGTH_SHORT).show()
                    blurLockView.visibility = View.INVISIBLE
                }
                Log.d(TAG, "Called when a biometric is recognized")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                activity!!.runOnUiThread {
                    Toast.makeText(activity, getString(R.string.biometric_failed), Toast.LENGTH_SHORT).show()
                }
                Log.d(TAG, "Called when a biometric is valid but not recognized")
            }
        })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometric_title))
            .setSubtitle(getString(R.string.biometric_subtitle))
            .setNegativeButtonText(getString(R.string.base_cancel))
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
