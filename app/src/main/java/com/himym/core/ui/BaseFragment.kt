package com.himym.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.himym.core.extension.layoutToDataBinding
import com.himym.core.helper.KLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * @author himym.
 * @description base fragment
 */
abstract class BaseFragment<VB : ViewDataBinding> : Fragment(), CoroutineScope by MainScope(), KLogger {

    protected lateinit var mBinding: VB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = layoutId().layoutToDataBinding(inflater, container)
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenFlowEvents()
        listenFlowStates()
        initFragment(view, savedInstanceState)
        bindToDBV()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
        cancel()
    }

    abstract fun layoutId(): Int

    abstract fun initFragment(view: View, savedInstanceState: Bundle?)

    open fun bindToDBV() {}

    open fun listenFlowStates() {}

    open fun listenFlowEvents() {}
}