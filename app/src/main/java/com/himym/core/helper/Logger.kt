package com.himym.core.helper

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

// 是否处于调试模式
internal var isDebugMode = true

// 单例 KLogger 实例
val kLogger = kLogger<KLogger>()

interface KLogger {

    val loggerTag: String get() = getTag(javaClass)
    val className: String get() = javaClass.simpleName
}

// 为指定的类生成 KLogger 实例
fun kLogger(clazz: Class<*>): KLogger = object : KLogger {
    override val loggerTag: String get() = getTag(clazz)
}

// 通过泛型为当前类生成 KLogger 实例
inline fun <reified T : Any> kLogger(): KLogger = kLogger(T::class.java)

// 获取类名，限制长度为 23 字符以符合 Log 的 tag 长度限制
private fun getTag(clazz: Class<*>) = clazz.simpleName.let {
    if (it.length <= 23) it else it.substring(0, 23)
}

// 获取调用堆栈信息，跳过系统和 KLogger 自身的堆栈，找到外部调用类
private fun getStackTraceInfo(): String {
    val stackTrace = Exception().stackTrace // 使用 Exception() 而非 Thread.currentThread()
    var element: StackTraceElement? = null

    // 跳过 KLogger 类和系统级类
    for (i in stackTrace.indices) {
        if (stackTrace[i].className != KLogger::class.java.name &&
            !stackTrace[i].className.contains("kotlin.lang") &&
            !stackTrace[i].className.contains("sun.reflect") &&
            !stackTrace[i].className.contains("com.himym.core.helper")) {
            element = stackTrace[i]
            break
        }
    }

    element?.let {
        val fileName = it.fileName
        val lineNumber = it.lineNumber
        val methodName = it.methodName
        return "($fileName:$lineNumber)#$methodName"
    }
    return "(Unknown Source)"
}

// 格式化日志
private fun KLogger.createLog(message: Any, listLine: Boolean): String {
    val stackTraceInfo = getStackTraceInfo()
    return if (listLine) "[$stackTraceInfo] $message" else "$className: [$stackTraceInfo] $message"
}

// 日志级别打印方法（重载支持 Throwable 和消息 lambda）
fun KLogger.vPrint(throwable: Throwable? = null, message: () -> Any?) {
    if (!isDebugMode) return
    val logMessage = createLog(message().toString(), false)
    if (throwable != null) {
        Log.v(loggerTag, logMessage, throwable)
    } else {
        Log.v(loggerTag, logMessage)
    }
}

fun KLogger.dPrint(throwable: Throwable? = null, message: () -> Any?) {
    if (!isDebugMode) return
    val logMessage = createLog(message().toString(), false)
    if (throwable != null) {
        Log.d(loggerTag, logMessage, throwable)
    } else {
        Log.d(loggerTag, logMessage)
    }
}

fun KLogger.iPrint(throwable: Throwable? = null, message: () -> Any?) {
    if (!isDebugMode) return
    val logMessage = createLog(message().toString(), false)
    if (throwable != null) {
        Log.i(loggerTag, logMessage, throwable)
    } else {
        Log.i(loggerTag, logMessage)
    }
}

fun KLogger.wPrint(throwable: Throwable? = null, message: () -> Any?) {
    if (!isDebugMode) return
    val logMessage = createLog(message().toString(), false)
    if (throwable != null) {
        Log.w(loggerTag, logMessage, throwable)
    } else {
        Log.w(loggerTag, logMessage)
    }
}

fun KLogger.ePrint(throwable: Throwable? = null, message: () -> Any?) {
    if (!isDebugMode) return
    val logMessage = createLog(message().toString(), false)
    if (throwable != null) {
        Log.e(loggerTag, logMessage, throwable)
    } else {
        Log.e(loggerTag, logMessage)
    }
}

// JSON 格式化打印
fun KLogger.jsonPrint(errorLevel: Boolean = true, message: () -> String) {
    if (!isDebugMode) return

    val json = message()
    val stackTraceInfo = getStackTraceInfo()

    if (json.isBlank()) {
        Log.i(loggerTag, "Empty or Null json content")
        return
    }

    val jsonInformation = try {
        when {
            json.startsWith("{") && json.endsWith("}") -> JSONObject(json).toString(4)
            json.startsWith("[") && json.endsWith("]") -> JSONArray(json).toString(4)
            else -> json
        }
    } catch (e: Exception) {
        "${e.cause?.message}${System.lineSeparator()}: $json"
    }

    val logMessage = "[$stackTraceInfo] $jsonInformation"
    if (errorLevel) {
        Log.e(loggerTag, logMessage)
    } else {
        Log.i(loggerTag, logMessage)
    }
}