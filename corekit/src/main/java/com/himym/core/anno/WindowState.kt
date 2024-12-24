package com.himym.core.anno

import androidx.annotation.IntDef

/**
 * @author himym.
 * @description 窗口状态
 */
@IntDef(value = [WindowState.TRANSPARENT_STATUS_BAR, WindowState.NORMAL])
@Retention(AnnotationRetention.SOURCE)
annotation class WindowState {
    companion object {
        const val TRANSPARENT_STATUS_BAR = 0 // 透明状态栏
        const val NORMAL = 1 // 正常
    }
}