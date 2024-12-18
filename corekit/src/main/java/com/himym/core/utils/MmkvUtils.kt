package com.himym.core.utils

import android.os.Parcelable
import com.tencent.mmkv.MMKV

/**
 * @author himym.
 * @description MMKV 工具类
 */

private fun defaultMmkv(multiProcess: Boolean) =
    MMKV.defaultMMKV(if (multiProcess) MMKV.MULTI_PROCESS_MODE else MMKV.SINGLE_PROCESS_MODE, null)

private fun String.idMmkv(multiProcess: Boolean) =
    MMKV.mmkvWithID(this, if (multiProcess) MMKV.MULTI_PROCESS_MODE else MMKV.SINGLE_PROCESS_MODE)

fun findMmkv(id: String? = null, multiProcess: Boolean = false): MMKV? =
    (if (id.isNullOrBlank()) defaultMmkv(multiProcess) else id.idMmkv(multiProcess))

//////////////////////////////////////////////////////////////
// MMKV ENCODE AND DECODE ///////////////////////////////////
////////////////////////////////////////////////////////////
fun encodeString(
    key: String, value: String,
    id: String? = null, multiProcess: Boolean = false
) = findMmkv(id, multiProcess)?.encode(key, value)

fun decodeString(
    key: String, default: String = "",
    id: String? = null, multiProcess: Boolean = false
) = findMmkv(id, multiProcess)?.decodeString(key, default) ?: default

fun encodeInt(
    key: String, value: Int,
    id: String? = null, multiProcess: Boolean = false
) = findMmkv(id, multiProcess)?.encode(key, value)

fun decodeInt(
    key: String, default: Int = 0,
    id: String? = null, multiProcess: Boolean = false
) = findMmkv(id, multiProcess)?.decodeInt(key, default) ?: default

fun encodeLong(
    key: String, value: Long,
    id: String? = null, multiProcess: Boolean = false
) = findMmkv(id, multiProcess)?.encode(key, value)

fun decodeLong(
    key: String, default: Long = 0L,
    id: String? = null, multiProcess: Boolean = false
) = findMmkv(id, multiProcess)?.decodeLong(key, default) ?: default

fun encodeFloat(
    key: String, value: Float,
    id: String? = null, multiProcess: Boolean = false
) = findMmkv(id, multiProcess)?.encode(key, value)

fun decodeFloat(
    key: String, default: Float = 0f,
    id: String? = null, multiProcess: Boolean = false
) = findMmkv(id, multiProcess)?.decodeFloat(key, default) ?: default

fun encodeDouble(
    key: String, value: Double,
    id: String? = null, multiProcess: Boolean = false
) = findMmkv(id, multiProcess)?.encode(key, value)

fun decodeDouble(
    key: String, default: Double = .0,
    id: String? = null, multiProcess: Boolean = false
) = findMmkv(id, multiProcess)?.decodeDouble(key, default) ?: default

fun encodeBoolean(
    key: String, value: Boolean,
    id: String? = null, multiProcess: Boolean = false
) = findMmkv(id, multiProcess)?.encode(key, value)

fun decodeBoolean(
    key: String, default: Boolean = false,
    id: String? = null, multiProcess: Boolean = false
) = findMmkv(id, multiProcess)?.decodeBool(key, default) ?: default

fun encodeBytes(
    key: String, value: ByteArray,
    id: String? = null, multiProcess: Boolean = false
) = findMmkv(id, multiProcess)?.encode(key, value)

fun decodeBytes(
    key: String, default: ByteArray = byteArrayOf(),
    id: String? = null, multiProcess: Boolean = false
) = findMmkv(id, multiProcess)?.decodeBytes(key, default) ?: default

fun encodeStringSet(
    key: String, value: Set<String>,
    id: String? = null, multiProcess: Boolean = false
) = findMmkv(id, multiProcess)?.encode(key, value)

fun decodeStringSet(
    key: String, default: Set<String> = setOf(),
    id: String? = null, multiProcess: Boolean = false
) = findMmkv(id, multiProcess)?.decodeStringSet(key, default) ?: default

fun <T : Parcelable> encodeParcelable(
    key: String, value: T,
    id: String? = null, multiProcess: Boolean = false
) = findMmkv(id, multiProcess)?.encode(key, value)

inline fun <reified T : Parcelable> decodeParcelable(
    key: String, default: T? = null,
    id: String? = null, multiProcess: Boolean = false
) = findMmkv(id, multiProcess)?.decodeParcelable(key, T::class.java, default) ?: default

// 清空所有数据
fun clearAll(id: String? = null, multiProcess: Boolean = false) = findMmkv(id, multiProcess)?.clearAll()