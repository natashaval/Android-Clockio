package com.natasha.clockio.friend.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.natasha.clockio.R
import com.natasha.clockio.home.entity.Department

class DepartmentAdapter constructor(private val departments: List<Department>): BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val row = layoutInflater.inflate(R.layout.item_department, parent, false)
        val departmentTextView: TextView = row.findViewById(R.id.addDepartmentInput) as TextView
        val dept = getItem(position) as Department
        departmentTextView.text = dept.name
        return row
    }

    override fun getItem(position: Int): Any = departments[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = departments.size
}