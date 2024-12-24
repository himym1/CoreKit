@file:Suppress("MemberVisibilityCanBePrivate")

package com.himym.core.helper

import android.app.Activity
import android.os.Process
import kotlin.system.exitProcess

/**
 * @author himym.
 * @description ActivityStackManager - 管理应用中所有 Activity 的堆栈操作
 */
object ActivityStackManager {

    // 持有所有 Activity 的列表
    private val activities = mutableListOf<Activity>()

    /** 添加 Activity 到管理栈 */
    fun addActivity(activity: Activity) {
        if (!activities.contains(activity)) {
            activities.add(activity)
        }
    }

    /** 从管理栈移除 Activity 并结束 */
    fun removeActivity(activity: Activity) {
        activities.removeAll { it == activity || it.isFinishing }
        activity.finish()
    }

    /** 结束所有 Activity 并清理堆栈 */
    fun finishAll() {
        activities.filterNot { it.isFinishing }.forEach { it.finish() }
        activities.clear() // 清理堆栈，避免持有无效引用
    }

    /** 退出应用 */
    fun exitApplication(killProcess: Boolean = true) {
        finishAll()
        if (killProcess) {
            Process.killProcess(Process.myPid())
            exitProcess(0)
        }
    }
}