package com.topping.core.base.toolbar

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import com.topping.core.base.BaseCommonActivity
import com.topping.core.extension.setOnDebounceClickListener
import com.topping.corekit.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Toolbar 工具类
 * 用于管理和控制应用程序顶部工具栏的显示和行为
 *
 * @author: wangjianguo
 * @date: 2024/12/27
 * @desc: 提供工具栏的各种状态管理和UI更新功能
 */
class ToolbarHelper(private val activity: BaseCommonActivity<*>) {
    // 工具栏状态的内部可变状态流，初始值为默认状态
    private val _toolbarState = MutableStateFlow<ToolbarState>(ToolbarState.Default(""))
    // 对外暴露的只读状态流
    val toolbarState: StateFlow<ToolbarState> = _toolbarState.asStateFlow()

    // 缓存工具栏中的视图引用
    private var currentViews: ToolbarViews? = null

    /**
     * 工具栏视图组件的数据类
     * 包含工具栏上所有可能用到的视图组件引用
     */
    private data class ToolbarViews(
        val ivBack: AppCompatImageView?,      // 返回按钮图标
        val tvTitle: AppCompatTextView?,      // 标题文本
        val ivRight: AppCompatImageView?,     // 右侧图标
        val tvRight: AppCompatTextView?,      // 右侧文本
        val switchRight: SwitchCompat?        // 右侧开关
    )

    /**
     * 设置工具栏状态并更新UI
     * @param state 要设置的工具栏状态
     */
    fun setup(state: ToolbarState) {
        // 首次调用时初始化视图引用
        if (currentViews == null) {
            currentViews = ToolbarViews(
                ivBack = activity.findViewById(R.id.ivBack),
                tvTitle = activity.findViewById(R.id.tvTitle),
                ivRight = activity.findViewById(R.id.ivRight),
                tvRight = activity.findViewById(R.id.tvRight),
                switchRight = activity.findViewById(R.id.switchRight)
            )
        }

        currentViews?.apply {
            // 设置基础配置：返回按钮点击事件和标题
            ivBack?.setOnClickListener { state.onBackClick?.invoke() ?: activity.onBackPressed() }
            tvTitle?.text = state.title

            // 根据不同状态配置工具栏UI
            when (state) {
                // 默认状态：隐藏所有右侧组件
                is ToolbarState.Default -> {
                    ivRight?.visibility = View.GONE
                    tvRight?.visibility = View.GONE
                    switchRight?.visibility = View.GONE
                }
                // 右侧文本按钮状态：显示右侧文本并设置点击事件
                is ToolbarState.WithRight -> {
                    ivRight?.visibility = View.GONE
                    tvRight?.apply {
                        visibility = View.VISIBLE
                        text = state.rightText
                        setOnDebounceClickListener { state.onRightClick() }
                    }
                    switchRight?.visibility = View.GONE
                }
                // 右侧开关状态：显示开关和可选的文本
                is ToolbarState.WithSwitch -> {
                    ivRight?.visibility = View.GONE
                    tvRight?.apply {
                        visibility = if (state.rightText != null) View.VISIBLE else View.GONE
                        text = state.rightText
                    }
                    switchRight?.apply {
                        visibility = View.VISIBLE
                        setOnCheckedChangeListener(null)  // 清除旧的监听器
                        isChecked = state.isChecked      // 设置开关状态
                        setOnCheckedChangeListener { _, isChecked ->  // 设置新的状态变化监听器
                            state.onSwitchChanged(isChecked)
                        }
                    }
                }
            }
        }

        // 更新状态流的值
        _toolbarState.value = state
    }

    /**
     * 更新工具栏状态
     * @param update 状态更新函数，接收当前状态并返回新状态
     */
    fun updateState(update: (ToolbarState) -> ToolbarState) {
        setup(update(_toolbarState.value))
    }
}