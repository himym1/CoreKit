package com.topping.core.utils

import android.os.Build

/**
 * @author himym.
 * @description actionsByR
 */

/**
 * 根据 Android 版本号执行不同的操作。
 * @param belowR 版本号小于 Android R 时执行
 * @param aboveR 版本号大于等于 Android R 时执行
 */
internal fun actionsByR(belowR: () -> Unit, aboveR: () -> Unit) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) belowR() else aboveR()
}

/**
 * @param version 版本号
 * @param below 版本号小于 version 时执行
 * @param aboveOrEqual 版本号大于等于 version 时执行
 */
internal fun actionsByVersion(
    version: Int = Build.VERSION_CODES.R,
    below: () -> Unit,
    aboveOrEqual: () -> Unit
) {
    if (Build.VERSION.SDK_INT < version) below() else aboveOrEqual()
}