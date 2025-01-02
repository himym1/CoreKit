package com.himym.core.extension

import android.view.animation.Animation
import android.animation.Animator

/**
 * 动画扩展方法，简化 Animation 和 Animator 的事件监听。
 *
 * 提供了 Kotlin 的 inline 高阶函数，用于更直观地处理动画的开始、结束、重复等事件。
 *
 * @author: himym
 * @description: AnimExtension
 */

/**
 * 为 Animation 添加监听器，当动画开始时执行指定的操作。
 *
 * @param action 动画开始时需要执行的逻辑
 */
inline fun Animation.doOnAnimStart(crossinline action: (animation: Animation?) -> Unit) =
    addAnimationListener(onAnimStart = action)

/**
 * 为 Animation 添加监听器，当动画结束时执行指定的操作。
 *
 * @param action 动画结束时需要执行的逻辑
 */
inline fun Animation.doOnAnimEnd(crossinline action: (animation: Animation?) -> Unit) =
    addAnimationListener(onAnimEnd = action)

/**
 * 为 Animation 添加监听器，当动画重复时执行指定的操作。
 *
 * @param action 动画重复时需要执行的逻辑
 */
inline fun Animation.doOnAnimRepeat(crossinline action: (animation: Animation?) -> Unit) =
    addAnimationListener(onAnimRepeat = action)

/**
 * 为 Animation 添加自定义监听器，可分别设置开始、结束和重复时的操作。
 *
 * @param onAnimStart 动画开始时执行的逻辑（默认不执行任何操作）
 * @param onAnimEnd 动画结束时执行的逻辑（默认不执行任何操作）
 * @param onAnimRepeat 动画重复时执行的逻辑（默认不执行任何操作）
 * @return 返回 Animation.AnimationListener 对象
 */
inline fun Animation.addAnimationListener(
    crossinline onAnimStart: (animation: Animation?) -> Unit = {},
    crossinline onAnimEnd: (animation: Animation?) -> Unit = {},
    crossinline onAnimRepeat: (animation: Animation?) -> Unit = {}
): Animation.AnimationListener {
    val listener = object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
            onAnimStart(animation)
        }

        override fun onAnimationEnd(animation: Animation?) {
            onAnimEnd(animation)
        }

        override fun onAnimationRepeat(animation: Animation?) {
            onAnimRepeat(animation)
        }
    }

    // 设置监听器
    setAnimationListener(listener)
    return listener
}

/**
 * 为 Animator 添加监听器，当动画开始时执行指定的操作。
 *
 * @param action 动画开始时需要执行的逻辑
 */
inline fun Animator.doOnStart(crossinline action: (animator: Animator?) -> Unit) =
    addAnimatorListener(onStart = action)

/**
 * 为 Animator 添加监听器，当动画结束时执行指定的操作。
 *
 * @param action 动画结束时需要执行的逻辑
 */
inline fun Animator.doOnEnd(crossinline action: (animator: Animator?) -> Unit) =
    addAnimatorListener(onEnd = action)

/**
 * 为 Animator 添加监听器，当动画被取消时执行指定的操作。
 *
 * @param action 动画被取消时需要执行的逻辑
 */
inline fun Animator.doOnCancel(crossinline action: (animator: Animator?) -> Unit) =
    addAnimatorListener(onCancel = action)

/**
 * 为 Animator 添加监听器，当动画重复时执行指定的操作。
 *
 * @param action 动画重复时需要执行的逻辑
 */
inline fun Animator.doOnRepeat(crossinline action: (animator: Animator?) -> Unit) =
    addAnimatorListener(onRepeat = action)

/**
 * 为 Animator 添加自定义监听器，可分别设置开始、结束、取消和重复时的操作。
 *
 * @param onStart 动画开始时执行的逻辑（默认不执行任何操作）
 * @param onEnd 动画结束时执行的逻辑（默认不执行任何操作）
 * @param onCancel 动画取消时执行的逻辑（默认不执行任何操作）
 * @param onRepeat 动画重复时执行的逻辑（默认不执行任何操作）
 * @return 返回 Animator.AnimatorListener 对象
 */
inline fun Animator.addAnimatorListener(
    crossinline onStart: (animator: Animator?) -> Unit = {},
    crossinline onEnd: (animator: Animator?) -> Unit = {},
    crossinline onCancel: (animator: Animator?) -> Unit = {},
    crossinline onRepeat: (animator: Animator?) -> Unit = {}
): Animator.AnimatorListener {
    val listener = object : Animator.AnimatorListener {

        override fun onAnimationStart(animation: Animator) {
            onStart(animation)
        }

        override fun onAnimationEnd(animation: Animator) {
            onEnd(animation)
        }

        override fun onAnimationCancel(animation: Animator) {
            onCancel(animation)
        }

        override fun onAnimationRepeat(animation: Animator) {
            onRepeat(animation)
        }
    }

    // 添加监听器
    addListener(listener)
    return listener
}