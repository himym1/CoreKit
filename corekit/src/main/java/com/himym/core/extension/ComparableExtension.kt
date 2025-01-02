package com.topping.core.extension

/**
 *  Comparable 扩展方法
 */

/**
 * 如果当前值小于指定阈值，则返回指定的替代值。
 * @param threshold 指定的阈值
 * @param alternative 当当前值小于指定阈值时，返回的替代值
 * @return 返回当前值或替代值
 */
fun <T : Comparable<T>> T.returnIfLess(threshold: T, alternative: () -> T): T =
    if (compareTo(threshold) < 0) alternative() else this

/**
 *  如果当前值等于指定阈值，则返回指定的替代值。
 *  @param threshold 指定的阈值
 *  @param alternative 当当前值等于指定阈值时，返回的替代值
 *  @return 返回当前值或替代值
 */
fun <T : Comparable<T>> T.returnIfEqual(threshold: T, alternative: () -> T): T =
    if (compareTo(threshold) == 0) alternative() else this

/**
 *  如果当前值大于指定阈值，则返回指定的替代值。
 *  @param threshold 指定的阈值
 *  @param alternative 当当前值大于指定阈值时，返回的替代值
 *  @return 返回当前值或替代值
 */
fun <T : Comparable<T>> T.returnIfLarger(threshold: T, alternative: () -> T): T =
    if (compareTo(threshold) > 0) alternative() else this

/**
 * 如果当前值满足指定条件，则返回指定的替代值。
 * @param condition 指定的条件
 * @param other 用于比较的值
 * @param alternative 当当前值满足指定条件时，返回的替代值
 * @return 返回当前值或替代值
 */
fun <T> T.returnIf(condition: (T, T) -> Boolean, other: T, alternative: () -> T): T =
    if (condition(this, other)) alternative() else this