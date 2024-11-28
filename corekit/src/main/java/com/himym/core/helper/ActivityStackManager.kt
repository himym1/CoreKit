@file:Suppress("MemberVisibilityCanBePrivate")

package com.himym.core.helper

import android.app.Activity
import android.os.Process
import kotlin.system.exitProcess

/**
 * @author himym.
 * @description
 */
object ActivityStackManager {

    private val activities = mutableListOf<Activity>()

    fun addActivity(activity: Activity) = activities.add(activity)

    fun removeActivity(activity: Activity) {
        if (activities.contains(activity)) {
            activities.remove(activity)
            activity.finish()
        }
    }

    fun finishAll() = activities.filterNot { it.isFinishing }.forEach { it.finish() }

    fun exitApplication(killProcess: Boolean = true) {
        finishAll()
        if (killProcess) {
            Process.killProcess(Process.myPid())
            exitProcess(0)
        }
    }
}