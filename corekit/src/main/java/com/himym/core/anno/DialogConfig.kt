package com.himym.core.anno

import android.view.Gravity

/**
 * @author himym.
 * @description Dialog 配置注解
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class DialogConfig(

    // 宽度类型
    val widthType: Int = DialogSizeType.WRAP_SIZE,
    // 高度类型
    val heightType: Int = DialogSizeType.WRAP_SIZE,
    // 宽度比例
    val widthFraction: Float = 0.75f,
    // 高度比例
    val heightFraction: Float = 0f,
    // 位置
    val gravity: Int = Gravity.CENTER,
    // 背景颜色
    val backgroundColor: String = "#00000000"
)
