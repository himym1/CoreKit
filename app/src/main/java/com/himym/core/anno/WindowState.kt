package com.himym.core.anno

import androidx.annotation.IntDef

/**
 * @author himym.
 * @description
 */
@IntDef(value = [WindowState.TRANSPARENT_STATUS_BAR, WindowState.NORMAL])
@Retention(AnnotationRetention.SOURCE)
annotation class WindowState {
    companion object {
        const val TRANSPARENT_STATUS_BAR = 0
        const val NORMAL = 1
    }
}