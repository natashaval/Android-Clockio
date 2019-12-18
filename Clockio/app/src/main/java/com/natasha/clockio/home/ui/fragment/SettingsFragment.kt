package com.natasha.clockio.home.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceFragmentCompat
import com.natasha.clockio.R

class SettingsFragment: PreferenceFragmentCompat(){

    private val TAG: String = SettingsFragment::class.java.simpleName
    private val mListener: OnViewOpenedInterface? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        activity!!.actionBar?.setTitle(R.string.action_settings)
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

//    https://stackoverflow.com/questions/42000674/how-to-display-fragment-on-full-screen
    override fun onStart() {
        super.onStart()
        val i:OnViewOpenedInterface = activity as OnViewOpenedInterface
        i.onOpen()
    }

    override fun onStop() {
        super.onStop()
        val i:OnViewOpenedInterface = activity as OnViewOpenedInterface
        i.onClose()
    }
}