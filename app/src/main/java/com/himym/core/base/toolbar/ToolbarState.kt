package com.himym.core.base.toolbar

/**
 *
 * @author: wangjianguo
 * @date: 2024/12/27
 * @desc: 工具栏配置类，用于构建不同类型的工具栏状态
 */
sealed interface ToolbarState {
    val title: String
    val onBackClick: (() -> Unit)?

    data class Default(
        override val title: String,
        override val onBackClick: (() -> Unit)? = null
    ) : ToolbarState

    data class WithRight(
        override val title: String,
        val rightText: String,
        val onRightClick: () -> Unit,
        override val onBackClick: (() -> Unit)? = null
    ) : ToolbarState

    data class WithSwitch(
        override val title: String,
        val rightText: String? = null,
        val isChecked: Boolean = false,
        val onSwitchChanged: (Boolean) -> Unit,
        override val onBackClick: (() -> Unit)? = null
    ) : ToolbarState
}

fun toolbar(block: ToolbarConfiguration.() -> Unit): ToolbarState =
    ToolbarConfiguration().apply(block).build()