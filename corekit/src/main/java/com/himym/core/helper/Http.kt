@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.himym.core.helper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.himym.core.utils.ParseUtils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.coroutines.resumeWithException

/**
 * @author himym.
 * @description Http - 网络请求工具类，支持 GET、POST、PUT、DELETE 请求。
 */
class Http {
    private val wrapper = OkRequestWrapper()

    fun url(url: String): Http {
        check(isValidUrl(url)) { "Invalid URL: $url" }
        wrapper.baseUrl = url
        return this
    }

    fun body(body: RequestBody): Http {
        wrapper.requestBody = body
        return this
    }

    fun params(params: HashMap<String, Any>): Http {
        wrapper.params = params
        return this
    }

    fun headers(headers: HashMap<String, String>): Http {
        wrapper.headers = headers
        return this
    }

    fun catch(block: suspend (Throwable) -> Unit): Http {
        wrapper.onFail = { block(it) }
        return this
    }

    suspend fun <T> get(): T? = execute("GET")
    suspend fun <T> post(): T? = execute("POST")
    suspend fun <T> put(): T? = execute("PUT")
    suspend fun <T> delete(): T? = execute("DELETE")

    private suspend fun <T> execute(method: String): T? {
        wrapper.method = method
        return try {
            HttpSingle.instance().enqueueForResult(wrapper) as T
        } catch (e: Exception) {
            wrapper.onFail(e)
            null
        }
    }

    companion object {
        private fun isValidUrl(url: String): Boolean {
            return try {
                url.toHttpUrlOrNull() != null
            } catch (e: Exception) {
                false
            }
        }
    }
}

/**
 * dsl fot request
 */
suspend fun http(init: OkRequestWrapper.() -> Unit) {
    val wrapper = OkRequestWrapper().apply(init)

    check(wrapper.baseUrl.matches(urlRegex)) { "Illegal url" }

    HttpSingle.instance().executeForResult(wrapper)
}

inline fun <reified T> Response.checkResult(): T? {
    val response = this.body?.string() ?: ""
    return try {
        if (response.isBlank()) null
        else ParseUtils.instance().parseFromJson(response, T::class.java)
    } catch (e: Exception) {
        null
    }
}

inline fun <reified T> Response.checkList(): MutableList<T> {
    val response = this.body?.string() ?: ""
    return try {
        if (response.isBlank()) mutableListOf()
        else ParseUtils.instance().parseFromJson(
            response, object : TypeToken<MutableList<T>>() {}.type
        ) ?: mutableListOf()
    } catch (e: Exception) {
        mutableListOf()
    }
}

fun Response.checkText(): String = this.body?.string() ?: ""

/**
 * generate json request body
 * @receiver String
 * @return RequestBody
 */
fun String.toJsonRequestBody() = toRequestBody("application/json;charset=utf-8".toMediaType())

fun <T : Any> T.toJsonRequestBody() = ParseUtils.instance().parseToJson(this).toJsonRequestBody()

/**
 * generate form body
 * @receiver HashMap<String, String>
 * @return FormBody
 */
fun HashMap<String, String>.toFormBody() = FormBody.Builder()
    .apply { forEach { entry -> add(entry.key, entry.value) } }
    .build()

/**
 * files to multipart body
 * @receiver List<File>
 * @param params HashMap<String, String>
 * @param fileKey String
 * @return MultipartBody
 */
fun List<File>.toMultipartBody(params: HashMap<String, String>, fileKey: String = "file") =
    MultipartBody.Builder().setType(MultipartBody.FORM)
        .apply {
            forEach {
                val body = it.asRequestBody("application/json;charset=utf-8".toMediaType())
                addFormDataPart(fileKey, it.name, body)
            }

            params.forEach { entry -> addFormDataPart(entry.key, entry.value) }
        }.build()

/**
 * http dsl data class
 */
data class OkRequestWrapper(
    var baseUrl: String = "",
    var method: String = "get",
    var requestBody: RequestBody? = null,
    var params: HashMap<String, Any> = hashMapOf(),
    var headers: HashMap<String, String> = hashMapOf(),
    var onSuccess: suspend (Response) -> Unit = {},
    var onFail: suspend (Throwable) -> Unit = {}
)

/**
 * Request singleton
 */
class HttpSingle private constructor() {
    companion object : SingletonHelperArg0<HttpSingle>(::HttpSingle)

    private var mOkHttpClient: OkHttpClient? = null

    fun globalHttpClient(client: OkHttpClient?) {
        mOkHttpClient = client
    }

    suspend fun executeForResult(wrapper: OkRequestWrapper) {
        flow { emit(onExecute(wrapper)) }
            .catch { wrapper.onFail(it) }
            .collectLatest { wrapper.onSuccess(it) }
    }

    suspend fun enqueueForResult(wrapper: OkRequestWrapper) =
        suspendCancellableCoroutine { continuation ->
            val request = requestBuild(wrapper)
            val call = (mOkHttpClient ?: generateOkHttpClient()).also { mOkHttpClient = it }.newCall(request)

            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    continuation.resumeWith(Result.success(response))
                }
            })

            continuation.invokeOnCancellation { call.cancel() }
        }


    private fun onExecute(wrapper: OkRequestWrapper): Response {
        val request = requestBuild(wrapper)
        return (mOkHttpClient ?: generateOkHttpClient().also { mOkHttpClient = it })
            .newCall(request).execute()
    }

    private fun requestBuild(wrapper: OkRequestWrapper): Request {
        val requestBuilder = Request.Builder()

        if (wrapper.headers.isNotEmpty()) {
            wrapper.headers.forEach { entry ->
                requestBuilder.addHeader(entry.key, entry.value)
            }
        }

        val request = when (wrapper.method.lowercase(Locale.getDefault())) {
            "post" -> requestBuilder.url(wrapper.baseUrl).post(
                wrapper.requestBody ?: generateRequestBody(wrapper.params)
            ).build()

            "put" -> requestBuilder.url(wrapper.baseUrl).put(
                wrapper.requestBody ?: generateRequestBody(wrapper.params)
            ).build()

            "delete" -> requestBuilder.url(wrapper.baseUrl).delete(
                wrapper.requestBody ?: generateRequestBody(wrapper.params)
            ).build()

            else -> requestBuilder.url(generateGetUrl(wrapper.params, wrapper.baseUrl))
                .get().build()
        }
        return request
    }

    //region generate http params
    private fun generateGetUrl(params: HashMap<String, Any>, url: String) =
        if (url.contains("?")) url
        else {
            val urlSb = StringBuilder(url).append("?")
            if (params.isNotEmpty()) {
                params.forEach { entry ->
                    val value = entry.value
                    urlSb.append(entry.key).append("=")
                        .append(if (value is String) value else Gson().toJson(value))
                        .append("&")
                }
            }
            urlSb.substring(0, urlSb.length - 1).toString()
        }

    private fun generateRequestBody(params: HashMap<String, Any>) =
        FormBody.Builder().apply {
            if (params.isNotEmpty()) params.forEach { entry ->
                val value = entry.value
                add(
                    entry.key,
                    if (value is String) value else ParseUtils.instance().parseToJson(value)
                )
            }
        }.build()
    //endregion
}