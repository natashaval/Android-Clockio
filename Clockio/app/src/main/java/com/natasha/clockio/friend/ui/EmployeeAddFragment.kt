package com.natasha.clockio.friend.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.natasha.clockio.R
import com.natasha.clockio.base.ui.SweetAlertConfirmListener
import com.natasha.clockio.base.ui.alertConfirmDelete
import com.natasha.clockio.base.util.observeOnce
import com.natasha.clockio.home.entity.Department
import com.natasha.clockio.home.ui.HomeActivity
import com.natasha.clockio.home.ui.fragment.OnViewOpenedInterface
import com.natasha.clockio.home.viewmodel.EmployeeViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_employee_add.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.File
import java.io.IOException
import javax.inject.Inject

class EmployeeAddFragment : Fragment() {

  companion object {
    fun newInstance() = EmployeeAddFragment()
    private val TAG: String = EmployeeAddFragment::class.java.simpleName
    private const val LOAD_IMAGE_FROM_GALLERY = 12
  }

  @Inject lateinit var factory: ViewModelProvider.Factory
  private lateinit var viewModel: EmployeeViewModel
  private lateinit var deptAdapter: DepartmentAdapter

  private var imageListener: SweetAlertConfirmListener? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val act = activity as HomeActivity
    act.supportActionBar?.setTitle(R.string.emp_add_title)
    return inflater.inflate(R.layout.fragment_employee_add, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this, factory).get(EmployeeViewModel::class.java)
    setHasOptionsMenu(true)

    observeDepartment()
    addImageClick()
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

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.save, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId) {
      R.id.action_save -> {
        Log.d(TAG, "employee created saved button clicked!")
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == LOAD_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
//      https://stackoverflow.com/questions/20197487/get-selected-image-from-gallery-into-imageview/20197713
      try {
        val imageUri = data.data
        imageUri?.let {
          addImageDisplay.setImageURI(it)
          showImageDisplay(true)
        }
      } catch (e: IOException) {
        Log.e(TAG, "failed to load image from gallery")
        e.printStackTrace()
      }
    }

  }

  private fun showDepartmentDropdown(departmentList: List<Department>) {
    deptAdapter = DepartmentAdapter(context!!,
      R.layout.item_department, departmentList)
    addDepartmentSpinner.adapter = deptAdapter

    addDepartmentSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
      override fun onNothingSelected(p0: AdapterView<*>?) {
      }

      override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val selectedDept = addDepartmentSpinner.selectedItem
        Log.d(TAG, "department selected $selectedDept")
      }
    }
  }

  private fun observeDepartment() {
    viewModel.getDepartment()
    viewModel.departmentList.observeOnce(this, Observer {
      Log.d(TAG, "deptList $it")
      showDepartmentDropdown(it)
    })
  }

  private fun addImageClick() {
    imageListener = object: SweetAlertConfirmListener {
      override fun onDelete(data: Any?) {
        Log.d(TAG, "onDelete in sweetAlert")
      }
    }
    showImageDisplay(false)
  }

  private fun showImageDisplay(isImageVisible: Boolean) {
    if (isImageVisible) {
      addImageDisplay.setOnClickListener {
        Log.d(TAG, "image clicked after posted")
        alertConfirmDelete(context!!, "Image will be replaced",
          imageListener as SweetAlertConfirmListener, null)
      }
    } else {
      addImageDisplay.setOnClickListener {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(i, LOAD_IMAGE_FROM_GALLERY)
      }
    }
  }

  private fun showFab(isShow: Boolean) {
//    if (isShow) {
//      val p = addImageInput.layoutParams
//    }
  }
}
