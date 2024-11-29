package com.himym.core.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * @author himym.
 * @description View 扩展函数
 */

/**
 * 隐藏软键盘
 */
fun View.hideSoftInput() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * 强制显示软键盘
 */
fun View.showSoftInputForce() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.showSoftInput(this, InputMethodManager.SHOW_FORCED)
}
