package com.himym.core.base

import android.app.Application
import android.content.Context

/**
 *
 * @author: wangjianguo
 * @date: 2024/11/26
 * @desc: 基础Application
 */
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}