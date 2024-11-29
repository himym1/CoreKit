package com.himym.core.helper

import android.annotation.TargetApi
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.himym.core.anno.PublicDirectoryType
import com.himym.core.utils.getMimeTypeByFile
import java.io.*

/**
 * @author himym.
 * @description FileCopy - 提供文件复制到公共目录的工具方法，支持 Android 各版本。
 */

////////////////////////////////////////////////////////////////////////////
// FileCopyBelowQ //////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////

/**
 * 复制文件 - 针对 Android Q 以下版本使用传统文件系统的方式。
 *
 * @param srcFile 源文件
 * @param dstFile 目标文件
 */
fun copyFileBelowQ(srcFile: File, dstFile: File) {
    srcFile.copyTo(dstFile, overwrite = true)
}

////////////////////////////////////////////////////////////////////////////
// FileCopyForQAndAbove(SAF) ///////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////

/**
 * 复制文件到公共图片目录 - 仅适用于 Android Q 及以上版本
 */
@TargetApi(Build.VERSION_CODES.Q)
fun Context.copyFileToPublicPictureOnQ(
    oriPrivateFile: File, displayName: String,
    relativePath: String = "", mimeType: String? = null,
    overwriteIfTargetFileExists: Boolean = false, copyFailed: ((Throwable) -> Unit)? = null
) {
    copyFileToPublic(
        oriPrivateFile, displayName, relativePath, mimeType,
        PublicDirectoryType.PICTURES, overwriteIfTargetFileExists, copyFailed
    )
}

/**
 * 复制文件到公共视频目录 - 仅适用于 Android Q 及以上版本
 */
@TargetApi(Build.VERSION_CODES.Q)
fun Context.copyFileToPublicMoveOnQ(
    oriPrivateFile: File, displayName: String,
    relativePath: String = "", mimeType: String? = null,
    overwriteIfTargetFileExists: Boolean = false, copyFailed: ((Throwable) -> Unit)? = null
) {
    copyFileToPublic(
        oriPrivateFile, displayName, relativePath, mimeType,
        PublicDirectoryType.MOVIES, overwriteIfTargetFileExists, copyFailed
    )
}

/**
 * 复制文件到公共音乐目录 - 仅适用于 Android Q 及以上版本
 */
@TargetApi(Build.VERSION_CODES.Q)
fun Context.copyFileToPublicMusicOnQ(
    oriPrivateFile: File, displayName: String,
    relativePath: String = "", mimeType: String? = null,
    overwriteIfTargetFileExists: Boolean = false, copyFailed: ((Throwable) -> Unit)? = null
) {
    copyFileToPublic(
        oriPrivateFile, displayName, relativePath, mimeType,
        PublicDirectoryType.MUSICS, overwriteIfTargetFileExists, copyFailed
    )
}

/**
 * 复制文件到公共下载目录 - 仅适用于 Android Q 及以上版本
 */
@TargetApi(Build.VERSION_CODES.Q)
fun Context.copyFileToDownloadsOnQ(
    oriPrivateFile: File, displayName: String,
    relativePath: String = "", mimeType: String? = null,
    overwriteIfTargetFileExists: Boolean = false, copyFailed: ((Throwable) -> Unit)? = null
) {
    copyFileToPublic(
        oriPrivateFile, displayName, relativePath, mimeType,
        PublicDirectoryType.DOWNLOADS, overwriteIfTargetFileExists, copyFailed
    )
}

/**
 * 获取公共目录的实际相对路径。
 *
 * @param relativePath 自定义的相对路径
 * @param copyTarget 目标公共目录类型
 * @return 实际相对路径
 */
internal fun realRelativePath(
    relativePath: String = "",
    copyTarget: PublicDirectoryType = PublicDirectoryType.DOWNLOADS
): String {
    val basePath = when (copyTarget) {
        PublicDirectoryType.PICTURES -> Environment.DIRECTORY_PICTURES
        PublicDirectoryType.DOWNLOADS -> Environment.DIRECTORY_DOWNLOADS
        PublicDirectoryType.MOVIES -> Environment.DIRECTORY_MOVIES
        PublicDirectoryType.MUSICS -> Environment.DIRECTORY_MUSIC
    }
    return if (relativePath.isBlank()) basePath else "$basePath${File.separator}$relativePath"
}

/**
 * 复制文件到公共目录，自动选择适配的实现方式。
 *
 * @param oriFile 源文件
 * @param displayName 显示名称（文件名）
 * @param relativePath 目标目录的相对路径
 * @param mimeType 文件的 MIME 类型
 * @param copyTarget 目标目录类型
 * @param overwriteIfTargetFileExists 是否覆盖目标文件
 * @param copyFailed 复制失败时的回调
 * @return 目标文件的 File 对象（低版本）或 null（高版本不返回 File）
 */
fun Context.copyFileToPublic(
    oriFile: File, displayName: String, relativePath: String = "",
    mimeType: String? = null, copyTarget: PublicDirectoryType = PublicDirectoryType.DOWNLOADS,
    overwriteIfTargetFileExists: Boolean = false, copyFailed: ((Throwable) -> Unit)? = null
): File? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        copyFileToPublicDirectory(
            oriFile, displayName, relativePath, mimeType, copyTarget, copyFailed
        )
    } else {
        val targetDir = when (copyTarget) {
            PublicDirectoryType.DOWNLOADS -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            PublicDirectoryType.MOVIES -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
            PublicDirectoryType.MUSICS -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
            PublicDirectoryType.PICTURES -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        }

        val storeFile = File(targetDir, "$relativePath/$displayName")
        try {
            oriFile.copyTo(storeFile, overwriteIfTargetFileExists)
        } catch (e: Exception) {
            copyFailed?.invoke(e)
            null
        }
    }
}

/**
 * 复制文件到公共目录 - 针对 Android Q 及以上版本。
 *
 * @param oriPrivateFile 源文件
 * @param displayName 显示名称（文件名）
 * @param relativePath 目标目录的相对路径
 * @param mimeType 文件的 MIME 类型
 * @param copyTarget 目标目录类型
 * @param copyFailed 复制失败时的回调
 * @return 目标文件的 File 对象或 null
 */
@TargetApi(Build.VERSION_CODES.Q)
internal fun Context.copyFileToPublicDirectory(
    oriPrivateFile: File, displayName: String,
    relativePath: String = "", mimeType: String? = null,
    copyTarget: PublicDirectoryType = PublicDirectoryType.DOWNLOADS,
    copyFailed: ((Throwable) -> Unit)? = null
): File? {
    val externalState = Environment.getExternalStorageState()
    val realReactivePath = realRelativePath(relativePath, copyTarget)

    // 设置文件的元数据信息
    val copyValues = ContentValues().apply {
        put(MediaStore.MediaColumns.TITLE, displayName)
        put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType ?: getMimeTypeByFile(oriPrivateFile.absolutePath))
        put(MediaStore.MediaColumns.DATE_TAKEN, System.currentTimeMillis())
        if (realReactivePath.isNotBlank()) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, realReactivePath)
        }
    }

    // 获取目标 URI
    val uri = when (copyTarget) {
        PublicDirectoryType.PICTURES -> contentResolver.insert(
            if (externalState == Environment.MEDIA_MOUNTED) MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            else MediaStore.Images.Media.INTERNAL_CONTENT_URI, copyValues
        )
        PublicDirectoryType.MOVIES -> contentResolver.insert(
            if (externalState == Environment.MEDIA_MOUNTED) MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            else MediaStore.Video.Media.INTERNAL_CONTENT_URI, copyValues
        )
        PublicDirectoryType.MUSICS -> contentResolver.insert(
            if (externalState == Environment.MEDIA_MOUNTED) MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            else MediaStore.Audio.Media.INTERNAL_CONTENT_URI, copyValues
        )
        PublicDirectoryType.DOWNLOADS -> contentResolver.insert(
            if (externalState == Environment.MEDIA_MOUNTED) MediaStore.Downloads.EXTERNAL_CONTENT_URI
            else MediaStore.Downloads.INTERNAL_CONTENT_URI, copyValues
        )
    }

    if (uri != null) {
        val buffer = ByteArray(8192) // 增加缓冲区大小以提升性能
        val bis = BufferedInputStream(FileInputStream(oriPrivateFile))
        val bos = BufferedOutputStream(contentResolver.openOutputStream(uri))

        try {
            var length = bis.read(buffer)
            while (length != -1) {
                bos.write(buffer, 0, length)
                length = bis.read(buffer)
            }
            bos.flush()
            return File(Environment.getExternalStorageDirectory(), "$realReactivePath/$displayName")
        } catch (e: Exception) {
            copyFailed?.invoke(e)
            return null
        } finally {
            bis.close()
            bos?.close()
        }
    } else {
        copyFailed?.invoke(NullPointerException("Uri is null"))
        return null
    }
}