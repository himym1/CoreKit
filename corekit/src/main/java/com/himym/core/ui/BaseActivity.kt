package com.topping.core.ui

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.topping.core.anno.ActivityConfig
import com.topping.core.anno.StatusBarTextColorMode
import com.topping.core.anno.WindowState
import com.topping.core.extension.hideStatusBar
import com.topping.core.extension.setStatusBarColor
import com.topping.core.extension.showStatusBar
import com.topping.core.helper.ActivityStackManager
import com.topping.core.helper.KLogger
import com.topping.core.helper.ePrint
import com.topping.core.utils.setStatusBarDarkMode
import com.topping.core.utils.setStatusBarLightMode
import com.topping.core.utils.translucentStatusBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.lang.reflect.ParameterizedType

/**
 * @author himym.
 * @description BaseActivity
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(),
    CoroutineScope by MainScope(), KLogger {

    protected lateinit var mBinding: VB
    protected val mActivityConfig by lazy<ActivityConfig?> { javaClass.getAnnotation(ActivityConfig::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityStackManager.addActivity(this)

        // 使用反射自动生成 ViewBinding 实例
        mBinding = inflateViewBinding()
        setContentView(mBinding.root)

        initStatusBar()
        listenFlowEvents()
        listenFlowStates()
        initActivity(savedInstanceState)
        bindToDBV()
    }

    /**
     * 利用反射绑定 ViewBinding
     */
    @Suppress("UNCHECKED_CAST")
    private fun inflateViewBinding(): VB {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        val clazz = type as Class<VB>
        val method = clazz.getMethod("inflate", android.view.LayoutInflater::class.java)
        return method.invoke(null, layoutInflater) as VB
    }

    private fun initStatusBar() {
        mActivityConfig?.let { config ->
            if (config.hideStatusBar) hideStatusBar() else showStatusBar()
            if (config.windowState == WindowState.TRANSPARENT_STATUS_BAR) {
                window.decorView.viewTreeObserver.addOnGlobalLayoutListener {
                    translucentStatusBar(config.contentUpToStatusBarWhenTransparent)
                }
            } else if (isValidColorFormat(config.statusBarColorString)) {
                setStatusBarColor(Color.parseColor(config.statusBarColorString))
            }
            if (config.statusBarTextColorMode == StatusBarTextColorMode.DARK) {
                setStatusBarLightMode()
            } else {
                setStatusBarDarkMode()
            }
        }
    }

    private fun isValidColorFormat(color: String): Boolean {
        return color.matches(Regex("#([0-9A-Fa-f]{8}|[0-9A-Fa-f]{6})"))
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
        ePrint { "BaseActivity canceled" }
        ActivityStackManager.removeActivity(this)
    }

    abstract fun initActivity(savedInstanceState: Bundle?)
    open fun bindToDBV() {}
    open fun listenFlowStates() {}
    open fun listenFlowEvents() {}
}