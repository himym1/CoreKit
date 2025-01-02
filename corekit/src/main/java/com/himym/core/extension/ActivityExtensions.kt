@file:Suppress("DEPRECATION")

package com.topping.core.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import com.topping.core.utils.actionsByR

/**
 * FragmentActivity 和 AppCompatActivity 的扩展方法，用于简化常见窗口和系统 UI 的操作。
 * 包括状态栏、导航栏以及全屏模式的处理。
 *
 * 支持 API 级别兼容，适用于 Android 低版本（<30）和高版本（>=30）的场景。
 *
 * @author: himym
 * @description: ActivityExtensions
 */

/**
 * 恢复正常窗口模式，重置系统 UI 标志或使用 WindowInsetsController 清除自定义外观。
 */
fun FragmentActivity.normalWindow() {
    actionsByR({
        // 低版本兼容方法（API < 30）
        window.decorView.systemUiVisibility = 0
    }, {
        // 高版本方法（API >= 30）
        window.insetsController?.setSystemBarsAppearance(0, 0)
    })
}

/**
 * 隐藏状态栏，支持低版本和高版本两种方式。
 */
fun FragmentActivity.hideStatusBar() {
    actionsByR({
        // 低版本兼容方法（API < 30）
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_FULLSCREEN
    }, {
        // 高版本方法（API >= 30）
        window.insetsController?.hide(WindowInsets.Type.statusBars())
    })

    // 隐藏 ActionBar 以保持一致的全屏体验
    actionBar?.hide()
}

/**
 * 隐藏导航栏，支持低版本和高版本两种方式。
 */
fun FragmentActivity.hideNavigationBar() {
    actionsByR({
        // 低版本兼容方法（API < 30）
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
    }, {
        // 高版本方法（API >= 30）
        window.insetsController?.hide(WindowInsets.Type.navigationBars())
    })
}

/**
 * 启用全屏沉浸模式，隐藏状态栏和导航栏。
 */
fun FragmentActivity.fullScreen() {
    actionsByR({
        // 低版本兼容方法（API < 30）
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }, {
        // 高版本方法（API >= 30）
        window.insetsController?.run {
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        }
    })

    // 隐藏 ActionBar 以保持一致的全屏外观
    actionBar?.hide()
}

/**
 * 退出全屏沉浸模式，显示状态栏和导航栏。
 */
fun FragmentActivity.exitFullScreen() {
    actionsByR({
        // 低版本兼容方法（API < 30）
        window.decorView.systemUiVisibility = 0
    }, {
        // 高版本方法（API >= 30）
        window.insetsController?.run {
            show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        }
    })

    // 显示 ActionBar
    actionBar?.show()
}

/**
 * 使用 Toolbar 替换默认的 ActionBar。
 * 支持自定义返回按钮和标题。
 *
 * @param toolbar 替换的 Toolbar
 * @param showBackButton 是否显示返回按钮（默认：true）
 * @param title 可选的标题（默认：null）
 */
fun AppCompatActivity.replaceActionBar(toolbar: Toolbar, showBackButton: Boolean = true, title: String? = null) {
    setSupportActionBar(toolbar)
    supportActionBar?.apply {
        setDisplayHomeAsUpEnabled(showBackButton)
        setDisplayShowTitleEnabled(title != null)
        title?.let { setTitle(it) }
    }
}

/**
 * 设置状态栏颜色。
 *
 * @param color 要应用的颜色
 */
fun FragmentActivity.setStatusBarColor(color: Int) {
    window.statusBarColor = color
}

/**
 * 检查状态栏是否可见。
 *
 * @return 如果状态栏可见返回 true，否则返回 false。
 */
fun FragmentActivity.isStatusBarVisible(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val insets = window.decorView.rootWindowInsets
        insets?.isVisible(WindowInsets.Type.statusBars()) ?: true
    } else {
        (window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_FULLSCREEN) == 0
    }
}

/**
 * 检查导航栏是否可见。
 *
 * @return 如果导航栏可见返回 true，否则返回 false。
 */
fun FragmentActivity.isNavigationBarVisible(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val insets = window.decorView.rootWindowInsets
        insets?.isVisible(WindowInsets.Type.navigationBars()) ?: true
    } else {
        (window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0
    }
}

@SuppressLint("InternalInsetResource")
fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun FragmentActivity.showStatusBar() {
    actionsByR({
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }, {
        window.insetsController?.show(WindowInsets.Type.statusBars())
    })
}

fun Activity.translucentStatusBar(contentUpToStatusBar: Boolean) {
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.statusBarColor = Color.TRANSPARENT
    window.decorView.systemUiVisibility = if (contentUpToStatusBar) {
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    } else {
        View.SYSTEM_UI_FLAG_VISIBLE
    }
}

fun Activity.setStatusBarLightMode() {
    actionsByR({
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }, {
        window.insetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    })
}

fun Activity.setStatusBarDarkMode() {
    actionsByR({
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }, {
        window.insetsController?.setSystemBarsAppearance(
            0,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    })
}