package com.himym.core.base

/**
 * @author himym
 * @description Toolbar配置
 */
data class ToolbarConfig(
    val title: String = "",
    val rightVisible: Boolean = false,
    val rightText: String = "",
    val rightIcon: Int = 0,
    val rightClick: () -> Unit = {}
)