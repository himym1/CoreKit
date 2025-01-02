package com.topping.core.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.topping.core.ui.BaseFragment

/**
 *
 * @author: wangjianguo
 * @date: 2024/12/3
 * @desc: 基础通用fragment
 */
abstract class BaseCommonFragment<VB:ViewBinding> : BaseFragment<VB>() {

    private val dialogTag = "loading_fragment"

    private val loadingManager by lazy { DefaultLoadingManager(FragmentHost(this)) }

    protected fun showLoading() = loadingManager.showLoading()

    protected fun hideLoading() = loadingManager.hideLoading()

    override fun initFragment(view: View, savedInstanceState: Bundle?) {
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