package com.natasha.clockio.base.ui

import android.content.Context
import android.util.Log
import cn.pedant.SweetAlert.SweetAlertDialog
import com.natasha.clockio.base.constant.AlertConst

fun alertSaved(context: Context, message: String?) {
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

fun alertSuccess(context: Context, message: String?) {
    SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
        .setTitleText(AlertConst.SUCCESS)
        .setContentText(message)
        .show()
}

fun alertConfirm(context: Context, message: String?, confirmText: String, listener: SweetAlertConfirmListener, data: Any?) {
    SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
        .setTitleText(AlertConst.CONFIRMATION)
        .setContentText(message)
        .setCancelText(AlertConst.CANCEL)
        .setConfirmButton(confirmText) { sDialog ->
            Log.d(message, "sweetAlert in alert confirm function")
            listener.onConfirm(data)

            sDialog.setTitleText(confirmText)
                .setContentText(null)
                .setConfirmText(AlertConst.OK)
                .showCancelButton(false)
                .setConfirmClickListener(null)
                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        }
        .show()
}

fun alertLoading(context: Context) {
    SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        .setTitleText(AlertConst.LOADING);
}

interface SweetAlertConfirmListener {
    fun onConfirm(data: Any?)
}