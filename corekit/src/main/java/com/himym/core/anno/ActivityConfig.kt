package com.topping.core.anno

/**
 * @author himym.
 * @description Activity 配置注解
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ActivityConfig(

    // 窗口状态
    val windowState: Int = WindowState.NORMAL,
    // 状态栏颜色字符串
    val statusBarColorString: String = "",
    // 状态栏文字颜色模式
    val statusBarTextColorMode: Int = StatusBarTextColorMode.LIGHT,
    // 透明时内容直至状态栏
    val contentUpToStatusBarWhenTransparent: Boolean = false,
    // 控制是否隐藏状态栏
    val hideStatusBar: Boolean = false
)