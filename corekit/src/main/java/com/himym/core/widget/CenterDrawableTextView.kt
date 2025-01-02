package com.topping.core.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.topping.core.helper.CenterDrawableHelper

/**
 * @author himym.
 * @description 图文居中的TextView
 */
open class CenterDrawableTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    //
    override fun onDraw(canvas: Canvas) {
        CenterDrawableHelper.preDraw(this, canvas)
        super.onDraw(canvas)
    }
}