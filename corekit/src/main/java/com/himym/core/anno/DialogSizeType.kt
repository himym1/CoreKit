package com.topping.core.anno

import androidx.annotation.IntDef

/**
 * @author himym.
 * @description Dialog 尺寸类型
 */
@IntDef(value = [DialogSizeType.FILL_SIZE, DialogSizeType.WRAP_SIZE])
@Retention(AnnotationRetention.SOURCE)
annotation class DialogSizeType {
    companion object {
        const val FILL_SIZE = 0 // 全屏
        const val WRAP_SIZE = 1 // 包裹
    }
}
