package com.natasha.clockio.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.natasha.clockio.R

//https://stackoverflow.com/questions/24422236/how-to-dynamically-populate-android-spinner-with-text-image/47390384#47390384
//http://android-er.blogspot.com/2010/12/custom-spinner-with-icon.html

class StatusSpinnerAdapter(context: Context, resource: Int, objects: Array<out String>, imageArray: Array<out Int>) :
    ArrayAdapter<String>(context, resource, objects) {

  private val stringArray = objects
  private val imageArray = imageArray

  override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
    return getCustomView(position, convertView, parent)
  }

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    return getCustomView(position, convertView, parent)
  }

  private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {

//    https://stackoverflow.com/questions/4382176/what-is-convertview-parameter-in-arrayadapter-getview-method
//    if (convertView == null) {
      val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
      val row = inflater.inflate(R.layout.item_status, parent, false)
//    }

    val statusText = row.findViewById<View>(R.id.statusText) as TextView
    statusText.text = stringArray[position]

    val statusIcon = row.findViewById<View>(R.id.statusIcon) as ImageView
    statusIcon.setImageResource(imageArray[position])

    return row
  }


}