package com.natasha.clockio.base.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

//https://proandroiddev.com/5-steps-to-implement-biometric-authentication-in-android-dbeb825aeee8
class BiometricUtils {
    fun isBiometricPromptEnabled(): Boolean {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
    }

    fun isSdkVersionSupported() : Boolean {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    }

    fun isPermissionGranted(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_BIOMETRIC) ==
                PackageManager.PERMISSION_GRANTED
    }
}