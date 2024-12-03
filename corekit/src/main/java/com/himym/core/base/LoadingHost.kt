package com.himym.core.base

import androidx.fragment.app.FragmentManager
import com.himym.core.ui.BaseFragment

/**
 *
 * @author: wangjianguo
 * @date: 2024/12/3
 * @desc:
 */
interface LoadingHost {
    val isHostActive: Boolean
    val fragmentManager: FragmentManager
}

class ActivityHost(private val activity: BaseCommonActivity<*>) : LoadingHost {
    override val isHostActive: Boolean
        get() = !activity.isFinishing && !activity.isDestroyed
    override val fragmentManager: FragmentManager
        get() = activity.supportFragmentManager
}

class FragmentHost(private val fragment: BaseFragment<*>) : LoadingHost {
    override val isHostActive: Boolean
        get() = fragment.isAdded && !fragment.isDetached
    override val fragmentManager: FragmentManager
        get() = fragment.childFragmentManager
}