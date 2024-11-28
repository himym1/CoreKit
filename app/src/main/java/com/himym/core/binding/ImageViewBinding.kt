package com.himym.core.binding

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.himym.core.abs.ImageLoadHelper

/**
 * @author himym.
 * @description BindingAdapter for Glide loading picture
 */
@BindingAdapter(value = ["imageData", "placeholderRes", "errorRes", "radius"], requireAll = false)
fun bindImageData(view: ImageView, imageData: Any?, placeholder: Drawable?, error: Drawable?, radius: Int?) {
    val engine = ImageLoadHelper.instance().engine
    check(engine != null) { "not set image load engine" }
    engine.loadImageData(view, imageData, placeholder, error, radius)
}

@BindingAdapter("backgroundData")
fun bindBackgroundData(view: View, backgroundData: Any) {
    val engine = ImageLoadHelper.instance().engine
    check(engine != null) { "not set image load engine" }
    engine.loadBackgroundData(view, backgroundData)
}