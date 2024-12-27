package com.himym.core.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.himym.core.base.toolbar.ToolbarHelper
import com.himym.core.base.toolbar.ToolbarState
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

    protected val toolbarHelper by lazy { ToolbarHelper(this) }

    protected open fun getInitialToolbarState(): ToolbarState? = null

    private val loadingManager by lazy { DefaultLoadingManager(ActivityHost(this)) }

    protected fun showLoading() = loadingManager.showLoading()

    protected fun hideLoading() = loadingManager.hideLoading()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getInitialToolbarState()?.let { state ->
            // 设置 Toolbar margin
            findViewById<View>(R.id.clToolBar)?.apply {
                val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.topMargin = getStatusBarHeight()
                setLayoutParams(layoutParams)
            }
            // 设置 Toolbar 状态
            toolbarHelper.setup(state)
        }

    }

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