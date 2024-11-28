package com.himym.core.extension

import android.widget.Toast
import com.himym.core.base.BaseApplication.Companion.appContext


/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/5/15 16:25
 * desc: Toast 扩展函数
 */

fun Any.toast(msg: Any = this.toString(), duration: Int = Toast.LENGTH_SHORT) {
    val message = when (msg) {
        is Int -> appContext.getString(msg) // 如果是资源 ID，获取对应的字符串
        is String -> msg // 如果是字符串，直接使用
        else -> msg.toString() // 其他情况，调用 `toString()`
    }
    Toast.makeText(appContext, message, duration).show()
}