package com.topping.core.extension

/**
 * 通用扩展函数，用于简化常见的逻辑判断和类型处理。
 *
 *  @author: himym
 *  @description: AnimExtension
 */

/**
 * 当 [bool] 为 true 时，执行 [block]，否则返回当前对象。
 */
fun <T> T.runIf(bool: Boolean, block: T.() -> T) = if (bool) run(block) else this

/**
 * 当 [bool] 为 false 时，执行 [block]，否则返回当前对象。
 */
fun <T> T.runUnless(bool: Boolean, block: T.() -> T) = if (bool) this else run(block)

/**
 * 当 [predicate] 为 true 时，返回 [ret]，否则返回当前对象。
 */
inline fun <reified T> T.let(predicate: Boolean, ret: T): T = if (predicate) ret else this

/**
 * 将当前对象强制转换为 [T] 类型。
 * 如果类型不匹配，将抛出 [ClassCastException]。
 */
inline fun <reified T> Any?.typeOf(): T = this as T

/**
 * 将当前对象尝试转换为 [T] 类型。
 * 如果类型不匹配，则返回 null。
 */
inline fun <reified T> Any?.typeOfOrNull(): T? = if (this is T) this else null

/**
 * 如果当前对象为 null，则抛出由 [asThrowable] 构造的异常。
 */
inline fun <reified T, R : Throwable> T?.throwNull(asThrowable: () -> R): T =
    this ?: throw asThrowable()

/**
 * 如果当前对象为 null，则抛出带有 [throwMessage] 的异常。
 */
inline fun <reified T> T?.throwNull(throwMessage: String): T =
    this ?: throw RuntimeException(throwMessage)

/**
 * 如果 [predicate] 为 true，则抛出由 [asThrowable] 构造的异常。
 */
inline fun <reified T, R : Throwable> T.throwIf(predicate: Boolean, asThrowable: T.() -> R) =
    takeUnless { predicate } ?: throw asThrowable(this)

/**
 * 如果由 [predicate] 返回的结果为 true，则抛出由 [asThrowable] 构造的异常。
 */
inline fun <reified T, R : Throwable> T.throwIf(
    predicate: T.() -> Boolean,
    asThrowable: T.() -> R
) =
    takeUnless { predicate(this) } ?: throw asThrowable(this)

/**
 * 如果 [predicate] 为 false，则抛出由 [asThrowable] 构造的异常。
 */
inline fun <reified T, R : Throwable> T.throwUnless(predicate: Boolean, asThrowable: T.() -> R) =
    takeIf { predicate } ?: throw asThrowable(this)

/**
 * 如果由 [predicate] 返回的结果为 false，则抛出由 [asThrowable] 构造的异常。
 */
inline fun <reified T, R : Throwable> T.throwUnless(
    predicate: T.() -> Boolean,
    asThrowable: T.() -> R
) =
    takeIf { predicate(this) } ?: throw asThrowable(this)

/**
 * 获取当前对象的类名。
 */
val Any.className: String get() = javaClass.simpleName

/**
 * 如果当前对象为 null，则返回 [orObj]。
 */
inline fun <reified T> T?.nullOr(orObj: T?): T? = this ?: orObj

/**
 * 如果当前对象为 null，则调用 [orGet] 并返回其结果。
 */
inline fun <reified T> T?.nullOr(orGet: () -> T?): T? = this ?: orGet()

/**
 * 数据类，用于存储元素及其索引。
 *
 * @param index 元素的索引位置
 * @param value 元素的值
 */
data class Index<T>(val index: Int, val value: T)