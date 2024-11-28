package com.himym.core.extension

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.himym.core.base.BaseApplication.Companion.appContext

/**
 * @author himym.
 * @description Resource extension
 */
fun Context.stringValue(@StringRes strRes: Int) = resources.getString(strRes)

fun Context.drawableValue(@DrawableRes drawRes: Int) =
    ContextCompat.getDrawable(this, drawRes) ?: ColorDrawable(Color.TRANSPARENT)

fun Context.colorValue(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun Context.dimenValue(@DimenRes dimenRes: Int) = resources.getDimension(dimenRes)

/**
 * example: get an drawable resource ic_launcher.png from drawable direction
 *
 * ```kotlin
 *    val iconLauncher = getResourceFromRawDirectory("ic_launcher.png", "drawable")
 * ```
 */
@SuppressLint("DiscouragedApi")
fun Context.getResourceFromRawDirectory(resourceName: String, directoryName: String) =
    resources.getIdentifier(resourceName, directoryName, packageName)


fun @receiver:StringRes Int.stringValue(): String {
    return appContext.getString(this)
}

fun @receiver:DrawableRes Int.drawableValue(): Drawable {
    return ContextCompat.getDrawable(appContext, this) ?: ColorDrawable(Color.TRANSPARENT)
}

fun @receiver:ColorRes Int.colorValue(): Int {
    return ContextCompat.getColor(appContext, this)
}

fun @receiver:DimenRes Int.dimenValue(): Float {
    return appContext.resources.getDimension(this)
}
