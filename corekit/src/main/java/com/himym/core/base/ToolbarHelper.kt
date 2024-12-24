package com.himym.core.base

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.himym.core.extension.setOnDebounceClickListener
import com.himym.corekit.R

/**
 *
 * @author: wangjianguo
 * @date: 2024/12/3
 * @desc: toolbar帮助类
 */
class ToolbarHelper(private val activity: BaseCommonActivity<*>) {

    fun setupToolbar(
        config: ToolbarConfig,
    ) {
        val ivBack = activity.findViewById<AppCompatImageView>(R.id.ivBack)
        val tvTitle = activity.findViewById<AppCompatTextView>(R.id.tvTitle)
        val ivRight = activity.findViewById<AppCompatImageView>(R.id.ivRight)
        val tvRight = activity.findViewById<AppCompatTextView>(R.id.tvRight)

        ivBack?.setOnClickListener { config.leftClick?.invoke() ?: activity.onBackPressed() }
        tvTitle?.text = config.title

        ivRight?.visibility = if (config.rightVisible) View.VISIBLE else View.GONE
        tvRight?.visibility = if (config.rightVisible) View.VISIBLE else View.GONE

        tvRight?.text = config.rightText
        ivRight?.setImageResource(config.rightIcon)
        tvRight?.setOnDebounceClickListener { config.rightClick() }
    }
}