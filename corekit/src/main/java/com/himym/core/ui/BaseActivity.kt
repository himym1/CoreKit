package com.himym.core.ui

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.himym.core.anno.ActivityConfig
import com.himym.core.anno.StatusBarTextColorMode
import com.himym.core.anno.WindowState
import com.himym.core.extension.d
import com.himym.core.extension.layoutToDataBinding
import com.himym.core.helper.ActivityStackManager
import com.himym.core.helper.KLogger
import com.himym.core.utils.setColorForStatusBar
import com.himym.core.utils.setStatusBarDarkMode
import com.himym.core.utils.setStatusBarLightMode
import com.himym.core.utils.translucentStatusBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * @author himym.
 * @description base activity
 */
abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity(),
    CoroutineScope by MainScope(), KLogger {

    protected val mBinding: VB by lazy { layoutId().layoutToDataBinding(this) }
    protected val mActivityConfig by lazy<ActivityConfig?> { javaClass.getAnnotation(ActivityConfig::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityStackManager.addActivity(this)

        mBinding.lifecycleOwner = this
        initStatusBar()
        listenFlowEvents()
        listenFlowStates()
        initActivity(savedInstanceState)
        bindToDBV()
    }

    private fun initStatusBar() {
        mActivityConfig?.let { config ->
            // 控制状态栏隐藏
            ImmersionBar.with(this)
                .apply {
                    if (config.hideStatusBar) {
                        hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
                    }
                    configureImmersionBar(this)
                }
                .init()

            // 设置窗口状态栏透明
            if (config.windowState == WindowState.TRANSPARENT_STATUS_BAR) {
                window.decorView.viewTreeObserver.addOnGlobalLayoutListener {
                    translucentStatusBar(config.contentUpToStatusBarWhenTransparent)
                }
            } else if (isValidColorFormat(config.statusBarColorString)) {
                setColorForStatusBar(Color.parseColor(config.statusBarColorString))
            }

            // 设置状态栏文本颜色模式
            if (config.statusBarTextColorMode == StatusBarTextColorMode.DARK) {
                setStatusBarLightMode()
            } else {
                setStatusBarDarkMode()
            }
        }
    }

    /**
     * 开放的配置方法，允许子类重写以扩展 ImmersionBar 配置
     * @param immersionBar 当前的 ImmersionBar 实例
     */
    open fun configureImmersionBar(immersionBar: ImmersionBar)  = Unit

    private fun isValidColorFormat(color: String): Boolean {
        return color.matches(Regex("#([0-9A-Fa-f]{8}|[0-9A-Fa-f]{6})"))
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
        "HomeFragment BaseActivity cancel".d()
        mBinding.unbind()
        ActivityStackManager.removeActivity(this)
    }

    abstract fun layoutId(): Int
    abstract fun initActivity(savedInstanceState: Bundle?)
    open fun bindToDBV() {}
    open fun listenFlowStates() {}
    open fun listenFlowEvents() {}
}