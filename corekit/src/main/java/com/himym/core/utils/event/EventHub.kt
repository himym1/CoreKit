package com.topping.core.utils.event


import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

/**
 *
 * @author: wangjianguo
 * @date: 2024/11/29
 * @desc: 事件总线，用于组件间通信
 */
object EventHub {

    // 使用线程安全的 Map 存储事件流
    private val eventsMap = mutableMapOf<String, MutableSharedFlow<Any>>()

    /**
     * 获取事件流，如果不存在则创建新的 MutableSharedFlow
     *
     * @param eventName 事件名称
     * @param replay    重放缓存大小，默认为 0，不缓存事件
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T> getEventFlow(eventName: String, replay: Int = 0): MutableSharedFlow<T> {
        return eventsMap.getOrPut(eventName) {
            MutableSharedFlow<Any>(
                replay = replay,
                extraBufferCapacity = Int.MAX_VALUE,
                onBufferOverflow = BufferOverflow.DROP_OLDEST
            )
        } as MutableSharedFlow<T>
    }

    /**
     * 发送事件
     *
     * @param eventName 事件名称
     * @param data      事件数据
     * @param isSticky  是否为 Sticky 事件，默认 false
     */
    fun <T> postEvent(eventName: String, data: T, isSticky: Boolean = false) {
        val flow = getEventFlow<T>(eventName, if (isSticky) 1 else 0)
        flow.tryEmit(data)
    }

    /**
     * 延迟发送事件
     *
     * @param eventName   事件名称
     * @param data        事件数据
     * @param delayMillis 延迟时间，毫秒
     * @param isSticky    是否为 Sticky 事件，默认 false
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun <T> postEventDelay(eventName: String, data: T, delayMillis: Long, isSticky: Boolean = false) {
        GlobalScope.launch {
            delay(delayMillis)
            postEvent(eventName, data, isSticky)
        }
    }

    /**
     * 订阅事件，自动与 LifecycleOwner 的生命周期绑定
     *
     * @param eventName  事件名称
     * @param isSticky   是否订阅 Sticky 事件，默认 false
     * @param minState   最小生命周期状态，默认为 STARTED
     * @param dispatcher 协程调度器，默认为 Main，即事件处理在主线程
     * @param action     事件处理函数
     */
    fun <T> LifecycleOwner.observeEvent(
        eventName: String,
        isSticky: Boolean = false,
        minState: Lifecycle.State = Lifecycle.State.STARTED,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        action: suspend (T) -> Unit
    ) {
        val flow = if (isSticky) {
            getEventFlow<T>(eventName, replay = 1)
        } else {
            getEventFlow<T>(eventName)
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(minState) {
                flow.collect { data ->
                    withContext(dispatcher) {
                        action(data)
                    }
                }
            }
        }
    }

    /**
     * 订阅事件，不依赖 LifecycleOwner，需要手动管理协程
     *
     * @param eventName  事件名称
     * @param isSticky   是否订阅 Sticky 事件，默认 false
     * @param dispatcher 协程调度器，默认为 Main
     * @param scope      协程作用域，默认为 GlobalScope
     * @param action     事件处理函数
     * @return           Job，可用于取消订阅
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun <T> observeEvent(
        eventName: String,
        isSticky: Boolean = false,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        scope: CoroutineScope = GlobalScope,
        action: suspend (T) -> Unit
    ): Job {
        val flow = if (isSticky) {
            getEventFlow<T>(eventName, replay = 1)
        } else {
            getEventFlow(eventName)
        }
        return scope.launch {
            flow.collect { data ->
                withContext(dispatcher) {
                    action(data)
                }
            }
        }
    }

    /**
     * 移除特定事件
     *
     * @param eventName 事件名称
     */
    fun removeEvent(eventName: String) {
        eventsMap.remove(eventName)
    }

    /**
     * 清除所有事件
     */
    fun clear() {
        eventsMap.clear()
    }
}