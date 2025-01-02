package com.topping.core.anno

/**
 * @author himym.
 * @description TextView Drawable 位置
 */
sealed class TextViewDrawableOrientation(val value: Int) {
    data object START : TextViewDrawableOrientation(0)
    data object TOP : TextViewDrawableOrientation(1)
    data object END : TextViewDrawableOrientation(2)
    data object BOTTOM : TextViewDrawableOrientation(3)
}