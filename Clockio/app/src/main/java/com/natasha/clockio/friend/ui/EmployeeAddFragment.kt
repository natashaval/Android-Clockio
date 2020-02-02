package com.natasha.clockio.friend.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider

import com.natasha.clockio.R
import com.natasha.clockio.home.entity.Department
import com.natasha.clockio.home.ui.HomeActivity
import com.natasha.clockio.home.ui.fragment.OnViewOpenedInterface
import com.natasha.clockio.home.viewmodel.EmployeeViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_employee_add.*
import javax.inject.Inject

class EmployeeAddFragment : Fragment() {

  companion object {
    fun newInstance() = EmployeeAddFragment()
    private val TAG: String = EmployeeAddFragment::class.java.simpleName
  }

  @Inject lateinit var factory: ViewModelProvider.Factory
  private lateinit var viewModel: EmployeeViewModel
  private lateinit var deptAdapter: ArrayAdapter<Department>
  private var departmentList = arrayListOf<Department>()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val act = activity as HomeActivity
    act.supportActionBar?.setTitle(R.string.emp_add_title)
    return inflater.inflate(R.layout.fragment_employee_add, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this, factory).get(EmployeeViewModel::class.java)

    departmentList.add(Department("1", "A", ""))
    departmentList.add(Department("2", "B", ""))
    departmentList.add(Department("3", "C", ""))
    setDepartmentDropdown()
  }

  override fun onAttach(context: Context) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  override fun onStart() {
    super.onStart()
    val i: OnViewOpenedInterface = activity as OnViewOpenedInterface
    i.onOpen()
  }

  override fun onStop() {
    super.onStop()
    val i: OnViewOpenedInterface = activity as OnViewOpenedInterface
    i.onClose()
  }

  private fun setDepartmentDropdown() {
    deptAdapter = ArrayAdapter(context!!, R.layout.item_department, R.id.addDepartmentInput, departmentList)
    addDepartmentSpinner.adapter = deptAdapter

    addDepartmentSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
      override fun onNothingSelected(p0: AdapterView<*>?) {

      }

      override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val selectedDept = addDepartmentSpinner.selectedItem as Department
        Log.d(TAG, "department selected $selectedDept")
      }

    }
  }
}
