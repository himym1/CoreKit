package com.himym.core.utils

import android.content.Context
import android.graphics.BitmapFactory

/**
 * @author himym.
 * @description Assets 文件加载工具类
 */

/**
 * 从 Assets 文件夹中加载文本文件
 * @param file 文件名
 * @return 文件内容
 */
fun Context.loadTextFromAssets(file: String): String = try {
    resources.assets.open(file).bufferedReader().use { it.readText() }
} catch (e: Exception) {
    ""
}

/**
 * 从 Assets 文件夹中加载图片文件
 * @param file 文件名
 * @return 图片 Bitmap
 */
fun Context.loadImageFromAssets(file: String) = try {
    resources.assets.open(file).use { BitmapFactory.decodeStream(it) }
} catch (e: Exception) {
    null
}