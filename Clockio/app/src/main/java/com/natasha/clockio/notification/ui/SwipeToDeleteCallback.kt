package com.natasha.clockio.notification.ui

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.natasha.clockio.R

abstract class SwipeToDeleteCallback(context: Context): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

  private val deleteIcon = context.getDrawable(R.drawable.ic_delete_black_24dp)
  private val intrinsicWidth = deleteIcon!!.intrinsicWidth
  private val intrinsicHeight = deleteIcon!!.intrinsicHeight
  private val background = ColorDrawable()
  private val backgroundColor = Color.RED
  private val clearPaint = Paint().apply {xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

  override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
      target: RecyclerView.ViewHolder): Boolean {
    return false
  }

//  https://github.com/kitek/android-rv-swipe-delete/blob/master/app/src/main/java/pl/kitek/rvswipetodelete/SwipeToDeleteCallback.kt
  override fun onChildDraw(c: Canvas, recyclerView: RecyclerView,
      viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int,
      isCurrentlyActive: Boolean) {
    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
      val itemView = viewHolder.itemView
      val itemHeight = itemView.bottom - itemView.top
      val isCanceled = dX == 0f && !isCurrentlyActive

      if(isCanceled) {
        clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        return
      }

      // Draw red delete background
      background.color = backgroundColor
      background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
      background.draw(c)

      // Calculate position of delete icon
      val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
      val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
      val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
      val deleteIconRight = itemView.right - deleteIconMargin
      val deleteIconBottom = deleteIconTop + intrinsicHeight

      // Draw the delete icon
      deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
      deleteIcon?.draw(c)

      super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
  }

  private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
    c?.drawRect(left, top, right, bottom, clearPaint)
  }
}