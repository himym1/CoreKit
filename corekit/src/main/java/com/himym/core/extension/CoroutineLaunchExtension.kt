package com.topping.core.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * @author himym.
 * @description 协程扩展函数
 */

/**
 *  Fragment 的生命周期作用域，支持在 Fragment 中启动协程
 *  @param context 协程上下文
 *  @param owner 生命周期所有者，默认为 viewLifecycleOwner
 *  @param state 生命周期状态，默认为 STARTED
 *  @param block 协程执行体
 */
inline fun Fragment.repeatLifecycleLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    owner: LifecycleOwner = viewLifecycleOwner,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    owner.lifecycleScope.launch(context) {
        viewLifecycleOwner.repeatOnLifecycle(state) {
            block()
        }
    }
}

/**
 *  AppCompatActivity 的生命周期作用域，支持在 AppCompatActivity 中启动协程
 *  @param context 协程上下文
 *  @param state 生命周期状态，默认为 STARTED
 *  @param block 协程执行体
 */
inline fun AppCompatActivity.repeatLifecycleLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch(context) {
        repeatOnLifecycle(state) { block() }
    }
}

/**
 *  启动协程
 *  @param context 协程上下文
 *  @param owner 生命周期所有者，默认为 viewLifecycleOwner
 *  @param block 协程执行体
 */
inline fun Fragment.launchForShared(
    context: CoroutineContext = EmptyCoroutineContext,
    owner: LifecycleOwner = viewLifecycleOwner,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    owner.lifecycleScope.launch(context) { block() }
}

/**
 *  启动协程
 *  @param context 协程上下文
 *  @param block 协程执行体
 */
inline fun AppCompatActivity.launchForShared(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch(context) { block() }
}

/**
 *  启动协程
 *  @param context 协程上下文
 *  @param block 协程执行体
 */
inline fun CoroutineScope.covLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend CoroutineScope.() -> Unit
) = launch(CoroutineExceptionHandler { _, _ -> } + context) { supervisorScope { block() } }

/**
 *  启动协程
 *  @param context 协程上下文
 *  @param errBlock 异常处理体
 *  @param block 协程执行体
 */
inline fun CoroutineScope.covLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline errBlock: suspend CoroutineScope.(Throwable) -> Unit = {},
    crossinline block: suspend CoroutineScope.() -> Unit
) = launch(CoroutineExceptionHandler { coroutineContext, throwable ->
    launch(coroutineContext) { errBlock(throwable) }
} + context) {
    supervisorScope { block() }
}

/**
 *  启动协程
 *  @param context 协程上下文
 *  @param onError 异常处理体
 *  @param onRun 协程执行体
 */
inline fun CoroutineScope.covLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline onError: suspend CoroutineScope.(CoroutineContext, Throwable) -> Unit = { _, _ -> },
    crossinline onRun: suspend CoroutineScope.() -> Unit
): Job = launch(
    CoroutineExceptionHandler { coroutineContext, throwable ->
        launch(context) { onError(coroutineContext, throwable) }
    } + context
) { supervisorScope { onRun() } }

/**
 *  ui线程执行协程
 *  @param block 协程执行体
 */
suspend fun <T> withUI(block: suspend CoroutineScope.() -> T) {
    withContext(Dispatchers.Main) { block() }
}

/**
 *  io线程执行协程
 *  @param block 协程执行体
 */
suspend fun <T> withIO(block: suspend CoroutineScope.() -> T) {
    withContext(Dispatchers.IO) { block() }
}

/**
 * 延迟启动协程
 * @param timeMills 延迟时间
 * @param context 协程上下文
 * @param block 协程执行体
 */
inline fun CoroutineScope.delayLaunch(
    timeMills: Long, context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend CoroutineScope.() -> Unit
): Job {
    check(timeMills >= 0) { "timeMills must be positive" }
    return launch(context) {
        delay(timeMills)
        block()
    }
}

/**
 * 启动一个重复执行的协程任务。
 *
 * @param interval 每次执行之间的时间间隔（毫秒）。必须大于 0。
 * @param repeatCount 重复执行的次数，默认为 [Int.MAX_VALUE] 表示无限重复。必须大于 0。
 * @param delayTime 启动任务前的延迟时间（毫秒），默认为 0，表示无启动延迟。
 * @param context 协程上下文，默认为 [EmptyCoroutineContext]，可以通过传递调度器（如 [Dispatchers.IO]）来指定执行线程。
 * @param block 重复执行的任务代码块，参数 [Int] 表示当前的执行次数（从 0 开始）。
 * @return 返回一个 [Job] 对象，可用于取消任务或检查任务状态。
 */
inline fun CoroutineScope.repeatLaunch(
    interval: Long, repeatCount: Int = Int.MAX_VALUE, delayTime: Long = 0L,
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend CoroutineScope.(Int) -> Unit,
): Job {
    check(interval > 0) { "timeDelta must be large than 0" }
    check(repeatCount > 0) { "repeat count must be large than 0" }

    return launch(context) {
        if (delayTime > 0) delay(delayTime)

        repeat(repeatCount) {
            block(it)
            delay(interval)
        }
    }
}