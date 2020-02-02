package com.natasha.clockio.friend.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.natasha.clockio.R
import com.natasha.clockio.home.entity.Department

//https://stackoverflow.com/questions/1625249/android-how-to-bind-spinner-to-custom-object-list
class DepartmentAdapter(context: Context, resource: Int, private val deptList: List<out Department>)
  : ArrayAdapter<Department>(context, resource, deptList) {
  override fun getCount(): Int = deptList.size
  override fun getItem(position: Int): Department? = deptList[position]
  override fun getItemId(position: Int): Long = position.toLong()

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    return getCustomView(position, convertView, parent)
  }

  override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
    return getCustomView(position, convertView, parent)
  }

  private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
    val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val row = inflater.inflate(R.layout.item_department, parent, false)
    val statusText = row.findViewById<View>(R.id.addDepartmentInput) as TextView
    statusText.text = deptList[position].name
    return row
  }
}