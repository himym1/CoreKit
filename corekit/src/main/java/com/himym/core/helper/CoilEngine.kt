package com.himym.core.helper

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.himym.core.abs.AbsImageEngine

/**
 * @author himym
 * @description Coil 图片加载引擎
 */
class CoilEngine : AbsImageEngine() {

    override fun loadImageData(
        view: ImageView,
        imageData: Any?,
        placeholderId: Drawable?,
        errorHolderId: Drawable?,
        radius: Int?
    ) {
        // 使用 Coil 的扩展函数来加载图片
        view.load(imageData) {
            // 设置占位符
            placeholder(placeholderId)

            // 设置加载失败时显示的图片
            error(errorHolderId)

            // 如果指定了圆角半径，则应用圆角变换
            radius?.let {
                transformations(RoundedCornersTransformation(it.toFloat()))
            }
        }
    }

    override fun loadBackgroundData(view: View, backgroundData: Any) {
        val context = view.context
        val imageLoader = ImageLoader(context)

        val request = ImageRequest.Builder(context)
            .data(backgroundData)
            .target(
                onStart = { placeholder ->
                    // 设置占位符为背景
                    view.background = placeholder
                },
                onSuccess = { drawable ->
                    // 成功加载时，将 Drawable 设置为背景
                    view.background = drawable
                },
                onError = { errorDrawable ->
                    // 加载失败时设置错误占位符
                    view.background = errorDrawable
                }
            )
            .build()

        // 使用 ImageLoader 执行请求
        imageLoader.enqueue(request)
    }
}