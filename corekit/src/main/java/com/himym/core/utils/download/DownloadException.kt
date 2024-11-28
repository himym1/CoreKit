package com.himym.core.utils.download

open class DownloadException(message: String) : Exception(message)

class ResponseFailedException(message: String = "response failed") : DownloadException(message)