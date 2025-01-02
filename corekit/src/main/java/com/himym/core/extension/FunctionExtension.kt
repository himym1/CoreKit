package com.topping.core.extension

/**
 * @author himym.
 * @description 函数扩展
 */

/**
 *  空函数
 */
val emptyAction: () -> Unit = {}

/**
 *  空消费者
 */
val emptyConsumer: (Any?) -> Unit = { _ -> }

/**
 *  空供应者
 *
 * @param  T 供应类型
 */
inline fun <T> T.applyBlock(block: (T) -> Unit) {
    block(this)
}