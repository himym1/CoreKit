package com.himym.core.utils.download.downloader

import com.himym.core.helper.iPrint
import com.himym.core.helper.kLogger
import com.himym.core.utils.download.DownloadException
import com.himym.core.utils.download.ResponseFailedException
import com.himym.core.utils.download.contentLength
import com.himym.core.utils.download.tmpFile
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.File
import java.io.RandomAccessFile
import java.text.DecimalFormat

/**
 * @author himym.
 * @description 断点续传下载器
 */
internal class RangeDownloader : AbsDownloader() {
    private var alreadyDownload = false

    override suspend fun download(response: Response, storeFile: File): Job? {
        val body = response.body
        if (body == null) {
            onDownloadFailed?.invoke(DownloadException("response body is null"))
            return null
        }

        val accessFile = beforeDownload(response, storeFile)
        return if (accessFile == null) {
            onProgressChange?.invoke(1f)
            onDownloadCompleted?.invoke()
            null
        } else {
            writeResponseToFile(body, accessFile, storeFile, response.contentLength())
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun writeResponseToFile(
        body: ResponseBody,
        accessFile: RandomAccessFile,
        storeFile: File,
        total: Long
    ): Job {
        return GlobalScope.launch(Dispatchers.IO) {
            val source = body.source()
            if (total <= 0) {
                onDownloadFailed?.invoke(ResponseFailedException("read content length error"))
                return@launch
            }

            val progressFormat = DecimalFormat("0.000")
            val buffer = ByteArray(1024 * 4)

            try {
                val tmpFile = storeFile.tmpFile()
                val realTotal = total + tmpFile.length()
                var downloadSize = tmpFile.length()
                var length: Int
                while (source.read(buffer).also { length = it } != -1) {
                    if (!isActive) {
                        source.close()
                        accessFile.close()
                        return@launch
                    }

                    accessFile.write(buffer, 0, length)
                    downloadSize += length
                    onProgressChange?.invoke(
                        progressFormat.format(downloadSize * 1f / realTotal).toFloat()
                    )
                }

                tmpFile.renameTo(storeFile)
                onDownloadCompleted?.invoke()
            } catch (e: Exception) {
                kLogger.iPrint{
                    e
                }
                onDownloadFailed?.invoke(e)
            } finally {
                source.close()
                accessFile.close()
            }
        }
    }

    private fun beforeDownload(response: Response, storeFile: File): RandomAccessFile? {
        if (storeFile.parentFile?.exists() == false) storeFile.parentFile?.mkdirs()

        if (storeFile.exists()) {
            if (storeFile.length() == response.contentLength()) {
                alreadyDownload = true
                return null
            }
            storeFile.delete()
        }

        val tmpFile = storeFile.tmpFile()

        return if (tmpFile.exists()) {
            if (tmpFile.length() == response.contentLength()) {
                tmpFile.renameTo(storeFile)
                alreadyDownload = true
                null
            } else {
                RandomAccessFile(tmpFile, "rw").apply { seek(tmpFile.length()) }
            }
        } else {
            RandomAccessFile(tmpFile.apply { createNewFile() }, "rw")
        }
    }
}