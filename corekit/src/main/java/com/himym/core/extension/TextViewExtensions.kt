package com.himym.core.extension

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.TextView
import com.himym.core.anno.TextViewDrawableOrientation

/**
 * @author himym.
 * @description TextView 扩展方法
 */

/** 设置 Drawable */
fun TextView.setDrawable(
    orientation: TextViewDrawableOrientation,
    drawableRes: Int? = null,
    path: String? = null,
    size: Int? = null,
    drawablePadding: Int? = null
) {
    appendDrawable(drawableRes, size, drawablePadding, orientation)
    appendDrawable(path, size, drawablePadding, orientation)
}

/** 内部实现：根据资源 ID 设置 Drawable */
internal fun TextView.appendDrawable(
    drawableRes: Int?,
    size: Int? = null,
    drawablePadding: Int? = null,
    orientation: TextViewDrawableOrientation = TextViewDrawableOrientation.START
) {
    if (drawablePadding != null) compoundDrawablePadding = drawablePadding

    val tarCompoundDrawables = compoundDrawables

    tarCompoundDrawables[orientation.value] =
        if (drawableRes == null) null
        else context.drawableValue(drawableRes).apply {
            setBounds(0, 0, size ?: intrinsicWidth, size ?: intrinsicHeight)
        }

    setCompoundDrawables(
        tarCompoundDrawables[0], tarCompoundDrawables[1],
        tarCompoundDrawables[2], tarCompoundDrawables[3]
    )
}

/** 内部实现：根据文件路径设置 Drawable */
internal fun TextView.appendDrawable(
    path: String?,
    size: Int? = null,
    drawablePadding: Int? = null,
    orientation: TextViewDrawableOrientation = TextViewDrawableOrientation.START
) {
    if (drawablePadding != null) compoundDrawablePadding = drawablePadding

    val tarCompoundDrawables = compoundDrawables

    tarCompoundDrawables[orientation.value] =
        if (path.isNullOrBlank()) null
        else {
            val bitmap = BitmapFactory.decodeFile(path)
            if (bitmap == null) {
                Log.e("TextViewExtension", "Failed to decode file: $path")
                return
            }
            BitmapDrawable(context.resources, bitmap).apply {
                setBounds(0, 0, size ?: intrinsicWidth, size ?: intrinsicHeight)
            }
        }

    setCompoundDrawables(
        tarCompoundDrawables[0], tarCompoundDrawables[1],
        tarCompoundDrawables[2], tarCompoundDrawables[3]
    )
}