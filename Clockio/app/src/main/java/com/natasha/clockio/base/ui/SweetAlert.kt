package com.natasha.clockio.base.ui

import android.content.Context
import android.util.Log
import cn.pedant.SweetAlert.SweetAlertDialog
import com.natasha.clockio.base.constant.AlertConst

fun alertSuccess(context: Context, message: String?) {
  SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
      .setTitleText(AlertConst.SAVED)
      .setContentText(message)
      .show()
}

fun alertFailed(context: Context, message: String?) {
  SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
      .setTitleText(AlertConst.FAILED)
      .setContentText(message)
      .show()
}

fun alertError(context: Context, message: String?) {
  SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
      .setTitleText(AlertConst.ERROR)
      .setContentText(message)
      .show()
}

fun alertConfirmDelete(context: Context, message: String?, listener: SweetAlertConfirmListener, data: Any?) {
    SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
        .setTitleText(AlertConst.CONFIRMATION)
        .setContentText(message)
        .setCancelText(AlertConst.CANCEL)
        .setConfirmButton(AlertConst.DELETE) { sDialog ->
            Log.d(message, "sweetAlert delete in alert confirm function")
            listener.onDelete(data)

            sDialog.setTitleText("Deleted!")
                .setContentText(null)
                .setConfirmText(AlertConst.OK)
                .showCancelButton(false)
                .setConfirmClickListener(null)
                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        }
        .show()
}

interface SweetAlertConfirmListener {
    fun onDelete(data: Any?)
}