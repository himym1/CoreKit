package com.himym.core.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.himym.core.extension.getStatusBarHeight
import com.himym.core.ui.BaseActivity
import com.himym.corekit.R

/**
 *  @author himym
 *  create at 2022/10/27 1:06
 *  description: 基础act
 */
abstract class BaseCommonActivity<VB : ViewBinding> : BaseActivity<VB>() {

    private val dialogTag = "loading_fragment"
    private val toolbarHelper by lazy { ToolbarHelper(this) }

    private val loadingManager by lazy { DefaultLoadingManager(ActivityHost(this)) }

    protected fun showLoading() = loadingManager.showLoading()

    protected fun hideLoading() = loadingManager.hideLoading()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getToolbarConfig()?.let { config ->
            findViewById<View>(R.id.clToolBar)?.apply {
                val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.topMargin = getStatusBarHeight()
                setLayoutParams(layoutParams)
            }
            toolbarHelper.setupToolbar(config)
        }

    }

    /**
     * 子类可覆盖此方法，返回 Toolbar 配置。
     * 如果返回 null，则不启用 Toolbar。
     */
    protected open fun getToolbarConfig(): ToolbarConfig? = null

    override fun initActivity(savedInstanceState: Bundle?) {
        initView()
        initData()
        initObserver()
    }

    // 初始化视图
    protected open fun initView() {}

    // 初始化数据
    protected open fun initData() {}

    // 初始化观察者
    protected open fun initObserver() {}

}