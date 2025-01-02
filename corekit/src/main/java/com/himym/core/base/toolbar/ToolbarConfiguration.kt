package com.topping.core.base.toolbar

/**
 *
 * @author: wangjianguo
 * @date: 2024/12/27
 * @desc: 工具栏配置类，用于构建不同类型的工具栏状态
 */
@ToolbarDsl
class ToolbarConfiguration {
    // 工具栏标题
    var title: String = ""
    // 返回按钮点击事件回调
    var onBackClick: (() -> Unit)? = null
    // 右侧文本内容
    var rightText: String? = null
    // 右侧按钮点击事件回调
    var onRightClick: (() -> Unit)? = null
    // 开关配置
    var switchConfig: SwitchConfig? = null

    /**
     * 开关配置内部类
     * 用于配置工具栏中的开关组件
     */
    @ToolbarDsl
    class SwitchConfig {
        // 开关状态（开/关）
        var isChecked: Boolean = false
        // 开关状态改变时的回调
        var onSwitchChanged: ((Boolean) -> Unit)? = null
    }

    /**
     * 配置开关组件的函数
     * @param block 开关配置的DSL代码块
     */
    fun switch(block: SwitchConfig.() -> Unit) {
        switchConfig = SwitchConfig().apply(block)
    }

    /**
     * 构建工具栏状态
     * @return 返回对应的工具栏状态：
     * 1. 带开关的工具栏（WithSwitch）
     * 2. 带右侧按钮的工具栏（WithRight）
     * 3. 默认工具栏（Default）
     */
    fun build(): ToolbarState = when {
        // 如果配置了开关，返回带开关的工具栏状态
        switchConfig != null -> ToolbarState.WithSwitch(
            title = title,
            rightText = rightText,
            isChecked = switchConfig!!.isChecked,
            onSwitchChanged = switchConfig!!.onSwitchChanged ?: {},
            onBackClick = onBackClick
        )
        // 如果配置了右侧文本和点击事件，返回带右侧按钮的工具栏状态
        rightText != null && onRightClick != null -> ToolbarState.WithRight(
            title = title,
            rightText = rightText!!,
            onRightClick = onRightClick!!,
            onBackClick = onBackClick
        )
        // 默认情况返回基础工具栏状态
        else -> ToolbarState.Default(
            title = title,
            onBackClick = onBackClick
        )
    }
}