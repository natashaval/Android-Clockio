package com.natasha.clockio.location.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.natasha.clockio.R
import com.natasha.clockio.base.constant.ParcelableConst
import com.natasha.clockio.location.entity.LocationModel

//https://stackoverflow.com/questions/43232516/how-to-reuse-recycle-googlemaps-in-fragment
//https://demonuts.com/android-google-map-in-fragment/
class MapsFragment : Fragment(), OnMapReadyCallback {

  private lateinit var mMap: GoogleMap
  private var location: LocationModel? = null
  private var locationHistory: ArrayList<LocationModel>? = null
  private var isSave: Boolean = false

  companion object {
    fun newInstance() = MapsFragment()
    val TAG: String = MapsFragment::class.java.simpleName
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?
  ): View? {
    val rootView =  inflater.inflate(R.layout.item_map, container, false)

    val mapFragment = childFragmentManager?.findFragmentById(R.id.map) as SupportMapFragment
    Log.d(TAG, "Map Created!")
    mapFragment.getMapAsync(this)

    arguments?.let {
      if (it.containsKey(ParcelableConst.LOCATION)) {
        location = it.getParcelable(ParcelableConst.LOCATION)
      }
      else if (it.containsKey(ParcelableConst.LOCATION_HISTORY)) {
        locationHistory =
          it.getParcelableArrayList<LocationModel>(ParcelableConst.LOCATION_HISTORY) as ArrayList<LocationModel>
      }
      isSave = it.getBoolean(ParcelableConst.LOCATION_SAVE)
      Log.d(TAG, "map receive location $location isSave $isSave")
      Log.d(TAG, "map receive location history size: ${locationHistory?.size}")

    }

    return rootView
  }

  override fun onMapReady(googleMap: GoogleMap) {
    mMap = googleMap
    mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
    mMap.clear()
    Log.d(TAG, "mMap ready!")

    if (isSave) {
      mMap.isMyLocationEnabled = true
    }

    // Add a marker in Sydney and move the camera
    location?.let {
      val loc = LatLng(it.latitude, it.longitude)
      mMap.addMarker(MarkerOptions().position(loc).title("My Location"))
      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15F))
    }

    locationHistory?.let {history ->
      if (history.size > 0) {
        var locHistoryLatLng = ArrayList<LatLng>()
        for (loc in history) {
          locHistoryLatLng.add(LatLng(loc.latitude, loc.longitude))
        }
        val polylineOptions = PolylineOptions().color(Color.RED)
          .startCap(RoundCap()).endCap(ButtCap())
          .addAll(locHistoryLatLng)
        mMap.addPolyline(polylineOptions)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locHistoryLatLng.last(), 15F))
      }
    }
  }
}
