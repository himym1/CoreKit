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
 * @description 资源扩展函数
 */

/**
 * 获取字符串资源。
 */
fun Context.stringValue(@StringRes strRes: Int): String = resources.getString(strRes)

/**
 * 获取 Drawable 资源。
 * 如果资源为 null，则返回透明的 ColorDrawable。
 */
fun Context.drawableValue(@DrawableRes drawRes: Int): Drawable =
    ContextCompat.getDrawable(this, drawRes) ?: ColorDrawable(Color.TRANSPARENT)

/**
 * 获取颜色资源。
 */
fun Context.colorValue(@ColorRes colorRes: Int): Int =
    ContextCompat.getColor(this, colorRes)

/**
 * 获取维度资源值（以 px 为单位）。
 */
fun Context.dimenValue(@DimenRes dimenRes: Int): Float =
    resources.getDimension(dimenRes)

/**
 * 根据资源名称和目录获取资源 ID。
 * 使用场景：动态加载资源。
 */
@SuppressLint("DiscouragedApi")
fun Context.getResourceFromRawDirectory(resourceName: String, directoryName: String): Int =
    resources.getIdentifier(resourceName, directoryName, packageName)

/**
 * 全局资源访问：获取字符串值。
 */
fun @receiver:StringRes Int.stringValue(): String = appContext.getString(this)

/**
 * 全局资源访问：获取 Drawable。
 */
fun @receiver:DrawableRes Int.drawableValue(): Drawable =
    ContextCompat.getDrawable(appContext, this) ?: ColorDrawable(Color.TRANSPARENT)

/**
 * 全局资源访问：获取颜色值。
 */
fun @receiver:ColorRes Int.colorValue(): Int =
    ContextCompat.getColor(appContext, this)

/**
 * 全局资源访问：获取维度值。
 */
fun @receiver:DimenRes Int.dimenValue(): Float =
    appContext.resources.getDimension(this)