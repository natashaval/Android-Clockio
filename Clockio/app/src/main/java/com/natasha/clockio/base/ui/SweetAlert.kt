package com.natasha.clockio.base.ui

import android.content.Context
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