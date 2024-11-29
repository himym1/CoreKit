package com.himym.core.extension

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

/**
 * @author himym.
 * @description Gson 扩展函数
 */

/**
 * 全局 Gson 实例，统一配置。
 */
object GsonProvider {
    val gson: Gson = GsonBuilder()
        .serializeNulls()
        .disableHtmlEscaping()
        .create()
}

/**
 * 从 JSON 字符串解析为指定类型的对象。
 */
inline fun <reified R> String.toObject(): R =
    GsonProvider.gson.fromJson(this, object : TypeToken<R>() {}.type)

/**
 * 尝试从 JSON 字符串解析为指定类型的对象，解析失败返回 null。
 */
inline fun <reified R> String.safeToObject(): R? =
    try {
        GsonProvider.gson.fromJson(this, object : TypeToken<R>() {}.type)
    } catch (e: Throwable) {
        null
    }

/**
 * 将对象序列化为 JSON 字符串。
 */
fun Any.toJson(): String = GsonProvider.gson.toJson(this)

/**
 * 安全地获取 JsonObject 中的指定字段值。
 */
fun JsonObject.getAsStringSafe(memberName: String): String? =
    if (has(memberName) && get(memberName).isJsonPrimitive) get(memberName).asString else null