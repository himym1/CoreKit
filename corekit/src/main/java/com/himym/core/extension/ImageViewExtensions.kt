package com.topping.core.extension

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.topping.core.abs.ImageLoadHelper
import com.topping.core.config.GlobalConfig

/**
 *
 * @author: wangjianguo
 * @date: 2024/12/3
 * @desc: ImageViewExtensions
 */

fun ImageView.loadImage(
    imageUrl: Any?,
    @DrawableRes placeholder: Int? = GlobalConfig.placeholder,
    @DrawableRes error: Int? = GlobalConfig.placeholder,
    radius: Int? = null
) {

    ImageLoadHelper.instance().loader.loadImageData(
        this,
        imageUrl,
        placeholder?.drawableValue(),
        error?.drawableValue(),
        radius
    )
}