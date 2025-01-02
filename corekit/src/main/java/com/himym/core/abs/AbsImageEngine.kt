package com.topping.core.abs

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.topping.core.helper.SingletonHelperArg0

/**
 * @author himym.
 * @description 图片加载引擎抽象类
 */
abstract class AbsImageEngine {
    abstract fun loadImageData(view: ImageView, imageData: Any?, placeholderId: Drawable? = null, errorHolderId: Drawable? = null, radius: Int? = null)

    abstract fun loadBackgroundData(view: View, backgroundData: Any)
}

class ImageLoadHelper {

    companion object : SingletonHelperArg0<ImageLoadHelper>(::ImageLoadHelper)

    var engine: AbsImageEngine? = null

    val loader: AbsImageEngine get() = engine!!
}