package com.natasha.clockio

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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

//https://stackoverflow.com/questions/43232516/how-to-reuse-recycle-googlemaps-in-fragment
//https://demonuts.com/android-google-map-in-fragment/
class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

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

        return rootView
    }

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mapFragment = childFragmentManager?.findFragmentById(R.id.map) as SupportMapFragment
        Log.d(TAG, "Map Created!")
//        val mapFragment = activity!!.supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        Log.d(TAG, "Map Not found!")
        mapFragment.getMapAsync(this)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "map onCreate")
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.item_map)
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
////        val mapFragment = fragmentManager?.findFragmentById(R.id.map) as SupportMapFragment
//        Log.d(TAG, "Map Created!")
//        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.clear()
        Log.d(TAG, "mMap ready!")

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}
