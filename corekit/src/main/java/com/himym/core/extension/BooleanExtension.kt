package com.topping.core.extension

/**
 * @author himym.
 * @description  布尔值扩展方法
 */
sealed class BooleanExtension<out T>

class DataTransformer<T>(val data: T) : BooleanExtension<T>()

data object Otherwise : BooleanExtension<Nothing>()

/**
 * 为 Boolean 添加扩展方法，简化条件判断逻辑。
 *
 * 提供了 Kotlin 的 inline 高阶函数，用于更直观地处理条件为 true 时的逻辑。
 *
 * @param block 条件为 true 时需要执行的逻辑
 * @return 返回执行结果
 */
inline fun <T> Boolean.yes(block: () -> T): BooleanExtension<T> =
    when {
        this -> DataTransformer(block())
        else -> Otherwise
    }

/**
 * 为 Boolean 添加扩展方法，简化条件判断逻辑。
 * @param block 条件为 false 时需要执行的逻辑
 * @return 返回执行结果
 */
inline fun <T> BooleanExtension<T>.otherwise(block: () -> T): T =
    when (this) {
        is Otherwise -> block()
        is DataTransformer<T> -> data
    }