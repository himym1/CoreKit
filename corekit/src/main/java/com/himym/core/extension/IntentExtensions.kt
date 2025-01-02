@file:Suppress("UNCHECKED_CAST")

package com.topping.core.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import java.io.Serializable
import kotlin.properties.ReadOnlyProperty

/**
 *
 * @author: wangjianguo
 * @date: 2024/11/29
 * @desc: Intent 扩展
 */

/**
 * 启动 Activity
 */
inline fun <reified T : Activity> Context.launchActivity(vararg extras: Pair<String, Any?>) {
    val intent = Intent(this, T::class.java).apply {
        extras.forEach { (key, value) ->
            when (value) {
                is Int -> putExtra(key, value)
                is String -> putExtra(key, value)
                is Boolean -> putExtra(key, value)
                is Parcelable -> putExtra(key, value)
                is Serializable -> putExtra(key, value)
                is List<*> -> {
                    if (value.isNotEmpty() && value.first() is Parcelable) {
                        putParcelableArrayListExtra(key, ArrayList(value as List<Parcelable>))
                    } else if (value.isNotEmpty() && value.first() is Serializable) {
                        putExtra(key, ArrayList(value as List<Serializable>))
                    } else {
                        throw IllegalArgumentException("Unsupported List type for key: $key")
                    }
                }
                else -> throw IllegalArgumentException("Unsupported extra type: ${value?.javaClass}")
            }
        }
    }
    startActivity(intent)
}

/**
 * 获取 Intent Extra
 */
inline fun <reified T> intentExtra(key: String, defaultValue: T): ReadOnlyProperty<Activity, T> {
    return ReadOnlyProperty { thisRef, _ ->
        val value = thisRef.intent?.extras?.get(key)
        when {
            value is T -> value
            value is ArrayList<*> && T::class == List::class -> value as T
            else -> defaultValue
        }
    }
}

/**
 * 获取 Intent Extra
 */
inline fun <reified T : Fragment> T.withArgs(vararg pairs: Pair<String, Any?>): T {
    arguments = Bundle().apply {
        pairs.forEach { (key, value) ->
            when (value) {
                is Int -> putInt(key, value)
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
                is Parcelable -> putParcelable(key, value)
                is Serializable -> putSerializable(key, value)
                is List<*> -> {
                    if (value.isNotEmpty() && value.first() is Parcelable) {
                        putParcelableArrayList(key, ArrayList(value as List<Parcelable>))
                    } else if (value.isNotEmpty() && value.first() is Serializable) {
                        putSerializable(key, ArrayList(value as List<Serializable>))
                    } else {
                        throw IllegalArgumentException("Unsupported List type for key: $key")
                    }
                }
                else -> throw IllegalArgumentException("Unsupported argument type: ${value?.javaClass}")
            }
        }
    }
    return this
}

/**
 * 获取 Fragment Argument
 */
inline fun <reified T> arg(key: String, defaultValue: T): ReadOnlyProperty<Fragment, T> {
    return ReadOnlyProperty { thisRef, _ ->
        val value = thisRef.arguments?.get(key)
        when {
            value is T -> value
            value is ArrayList<*> && T::class == List::class -> value as T
            else -> defaultValue
        }
    }
}