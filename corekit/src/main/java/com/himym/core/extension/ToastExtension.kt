package com.topping.core.extension

import android.content.Context
import android.view.View
import android.widget.Toast
import com.topping.core.base.BaseApplication.Companion.appContext

/**
 * @author himym.
 * @description Toast 扩展函数
 */

fun Context.toast(msg: Any, duration: Int = Toast.LENGTH_SHORT) {
    val message = when (msg) {
        is Int -> runCatching { getString(msg) }.getOrDefault("Invalid resource ID")
        is String -> msg
        else -> msg.toString()
    }
    Toast.makeText(this, message, duration).show()
}

// 扩展 View
fun View.toast(msg: Any, duration: Int = Toast.LENGTH_SHORT) {
    context.toast(msg, duration)
}

// 全局扩展 Any，需谨慎使用
fun Any.toast(msg: Any = this.toString(), duration: Int = Toast.LENGTH_SHORT) {
    val context = appContext
    val message = when (msg) {
        is Int -> runCatching { context.getString(msg) }.getOrDefault("Invalid resource ID")
        is String -> msg
        else -> msg.toString()
    }
    Toast.makeText(context, message, duration).show()
}