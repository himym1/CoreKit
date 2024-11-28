package com.himym.core.listener

/**
 * @author himym.
 * @description global throwable handler
 */
fun interface OnGlobalThrowableHandler {
    fun onGlobalThrowableHandler(throwable: Throwable)
}