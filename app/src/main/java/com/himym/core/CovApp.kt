package com.himym.core

import android.app.Application
import com.himym.core.abs.AbsImageEngine
import com.himym.core.abs.ImageLoadHelper
import com.himym.core.config.GlobalConfig
import com.himym.core.extension.DEFAULT_DEBOUNCE_TIME
import com.himym.core.helper.CoilEngine
import com.himym.core.helper.HttpSingle
import com.himym.core.helper.RequestConfig
import com.himym.core.helper.RetrofitHelper
import com.himym.core.helper.isDebugMode
import com.tencent.mmkv.MMKV
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author himym.
 * @description Initial Necessary params
 */
fun Application.startCov(covApp: CovApp.() -> Unit) {
    val covConfig = CovApp().apply(covApp)

    MMKV.initialize(this)

    isDebugMode = covConfig.openDebug

    DEFAULT_DEBOUNCE_TIME = covConfig.debounceTime

    globalHttpClient {
        baseUrl = covConfig.baseUrl
        client = covConfig.client
        customCallAdapter = covConfig.customRetrofitCallAdapterArray
        customConvertAdapter = covConfig.customRetrofitConverterFactoryArray
    }

    globalLoadEngine(covConfig.loadEngine)
    // 设置全局的 placeholder
    GlobalConfig.placeholder = covConfig.placeholder
}

data class CovApp(
    var openDebug: Boolean = true,
    var debounceTime: Long = 500,
    var koinPropertiesFile: String? = "",
    var baseUrl: String = "",
    var client: OkHttpClient? = null,
    var customRetrofitCallAdapterArray: MutableList<CallAdapter.Factory> = mutableListOf(),
    var customRetrofitConverterFactoryArray: MutableList<Converter.Factory> = mutableListOf(GsonConverterFactory.create()),
    var loadEngine: AbsImageEngine? = CoilEngine(),
    var placeholder: Int? = null,
)

///////////////////////////////
// Image Load Engine /////////
/////////////////////////////
fun globalLoadEngine(engine: AbsImageEngine?) {
    ImageLoadHelper.instance().engine = engine
}

////////////////////////////////
// Request Initial DSL ////////
//////////////////////////////
fun globalHttpClient(init: RequestConfig.() -> Unit) {
    val rqConfig = RequestConfig().apply(init)
    HttpSingle.instance().globalHttpClient(rqConfig.client)
    RetrofitHelper.instance().run {
        if (rqConfig.baseUrl.isNotBlank()) {
            setBaseUrl(rqConfig.baseUrl)
        }
        setClient(rqConfig.client)
        setCustomCallAdapter(rqConfig.customCallAdapter)
        setCustomConvertFactory(rqConfig.customConvertAdapter)
    }
}

