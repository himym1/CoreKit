package com.himym.core.extension

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.himym.core.abs.ImageLoadHelper
import com.himym.core.config.GlobalConfig

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