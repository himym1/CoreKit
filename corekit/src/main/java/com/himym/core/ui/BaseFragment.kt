package com.himym.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.himym.core.helper.KLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.lang.reflect.ParameterizedType

/**
 * @author himym.
 * @description BaseFragment
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment(), CoroutineScope by MainScope(), KLogger {

    private var _binding: VB? = null
    protected val mBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // 使用反射实例化 ViewBinding
        _binding = inflateViewBinding(inflater, container)
        return mBinding.root
    }

    @Suppress("UNCHECKED_CAST")
    private fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        val clazz = type as Class<VB>
        val inflateMethod = clazz.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.javaPrimitiveType
        )
        return inflateMethod.invoke(null, inflater, container, false) as VB
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenFlowEvents()
        listenFlowStates()
        initFragment(view, savedInstanceState)
        bindToDBV()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 防止内存泄漏
        cancel()
    }

    abstract fun initFragment(view: View, savedInstanceState: Bundle?)

    open fun bindToDBV() {}

    open fun listenFlowStates() {}

    open fun listenFlowEvents() {}
}