package com.natasha.clockio.location

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.SettingsClient
import com.natasha.clockio.home.ui.fragment.GPS_REQUEST

//https://github.com/mayowa-egbewunmi/LocationUpdateWithLiveData/blob/master/app/src/main/java/com/mayowa/android/locationwithlivedata/GpsUtils.kt
class GpsUtils(private val context: Context) {
    private val settingsClient: SettingsClient = LocationServices.getSettingsClient(context)
    private val locationSettingsRequest: LocationSettingsRequest?
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val TAG = GpsUtils::class.java.simpleName

    init {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(LocationLiveData.locationRequest)
        locationSettingsRequest = builder.build()
        builder.setAlwaysShow(true)
    }

    fun turnOnGps(onGpsListener: OnGpsListener?) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onGpsListener?.gpsStatus(true)
        } else {
            settingsClient
                .checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(context as Activity) {
                    // GPS is already enabled
                    onGpsListener?.gpsStatus(true)
                }
                .addOnFailureListener(context) { e ->
                    when((e as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                            try {
                                // show dialog by calling startResolutionForResult(), and check the result
                                val rae = e as ResolvableApiException
                                rae.startResolutionForResult(context, GPS_REQUEST)
                            } catch (sie: IntentSender.SendIntentException) {
                                Log.i(TAG, "PendingIntent unable to execute request.")
                            }

                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage = "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                            Log.e(TAG, errorMessage)

                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
    }

    interface OnGpsListener {
        fun gpsStatus(isGpsEnable: Boolean)
    }
}