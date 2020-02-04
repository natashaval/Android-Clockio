package com.natasha.clockio.base.ui

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import java.lang.IllegalArgumentException

fun customResolveOrThrow(context: Context, @AttrRes attributeResId: Int): Int {
  val typedValue = TypedValue()
  if (context.theme.resolveAttribute(attributeResId, typedValue, true)) {
    return typedValue.data
  }
  throw IllegalArgumentException(context.resources.getResourceName(attributeResId))
}