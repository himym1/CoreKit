package com.himym.core.utils

import android.content.Context
import android.content.Intent
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import java.io.File

/**
 * @author himym.
 * @description 文件打开工具类
 */

val DEFAULT_MIMETYPE = "*/*"

/**
 * 根据文件名获取 MIME 类型
 * @param fileName 文件名
 * @return MIME 类型
 */
fun getMimeTypeByFile(fileName: String): String {
    val extension = if (fileName.contains(".")) fileName.split(".").last() else ""
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: DEFAULT_MIMETYPE
}

/**
 * 根据 MIME 类型打开文件
 * @param file 文件
 * @param authority FileProvider authority
 * @param viewEmptyFile 是否查看空文件
 * @param handleUnknownBySystem 是否由系统处理未知 MIME 类型
 * @param unknownMimeType 未知 MIME 类型处理
 */
fun Context.openFileByMimeType(
    file: File, authority: String? = null,
    viewEmptyFile: Boolean = true,
    handleUnknownBySystem: Boolean = true,
    unknownMimeType: ((File) -> Unit)? = null
) {
    if (file.length() <= 0L && !viewEmptyFile) return

    val mimeType = getMimeTypeByFile(file.name)
    if (mimeType == DEFAULT_MIMETYPE && !handleUnknownBySystem) {
        unknownMimeType?.invoke(file) ?: return
        return
    }

    openFileByMimeType(file, mimeType, authority)
}

/**
 * 根据 MIME 类型打开文件
 * @param file 文件
 * @param mimeType MIME 类型
 * @param authority FileProvider authority
 */
fun Context.openFileByMimeType(file: File, mimeType: String, authority: String? = null) {
    try {
        val uri = FileProvider.getUriForFile(this, authority ?: "$packageName.fileprovider", file)

        startActivity(Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, mimeType)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        })
    } catch (e: Exception) {
        e.printStackTrace()
    }
}