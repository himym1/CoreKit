package com.himym.core.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.viewbinding.ViewBinding
import com.himym.core.extension.getStatusBarHeight
import com.himym.core.extension.setOnDebounceClickListener
import com.himym.core.ui.BaseActivity
import com.himym.main.R

/**
 *  @author himym
 *  create at 2022/10/27 1:06
 *  description: 基础act
 */
abstract class BaseCommonActivity<VB : ViewBinding> : BaseActivity<VB>() {

    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private val dialogTag = "loading_fragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 如果使用 Toolbar，并且需要调整状态栏
        if (useToolbar) {
            findViewById<View>(R.id.clToolBar)?.apply {
                // 设置 Toolbar 的顶部边距，避免被状态栏遮挡
                val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.topMargin = getStatusBarHeight()
                setLayoutParams(layoutParams)
            }
            initToolbar(toolbarConfig)
        }

        // 注册返回键处理
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!handleBackPressed()) {
                    if (isEnabled) {
                        isEnabled = false
                        onBackPressed()
                    }
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    // 子类可以覆盖此属性来决定是否使用 Toolbar，默认不使用
    protected open val useToolbar: Boolean = false

    // 抽象属性，子类必须实现此配置当 useToolbar 为 true 时
    protected open val toolbarConfig: ToolbarConfig
        get() = ToolbarConfig() // 默认返回一个空配置，当子类未覆盖时将显示默认内容

    // 初始化 Toolbar，使用 ToolbarConfig 数据类
    private fun initToolbar(config: ToolbarConfig) {
        setupToolbar(config)
    }

    // 设置 Toolbar
    private fun setupToolbar(config: ToolbarConfig) {
        val ivBack = findViewById<AppCompatImageView>(R.id.ivBack)
        val tvTitle = findViewById<AppCompatTextView>(R.id.tvTitle)
        val ivRight = findViewById<AppCompatImageView>(R.id.ivRight)
        val tvRight = findViewById<AppCompatTextView>(R.id.tvRight)

        ivBack?.setOnClickListener { onBackPressed() }
        tvTitle?.text = config.title

        ivRight?.visibility = if (config.rightVisible) View.VISIBLE else View.GONE
        tvRight?.visibility = if (config.rightVisible) View.VISIBLE else View.GONE

        tvRight?.text = config.rightText
        ivRight?.setImageResource(config.rightIcon)
        tvRight?.setOnDebounceClickListener { config.rightClick() }
    }
    /**
     * 子类可以重写此方法来处理返回事件
     * @return true 表示返回事件已被处理，false 表示未处理
     */
    open fun handleBackPressed(): Boolean {
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }

}