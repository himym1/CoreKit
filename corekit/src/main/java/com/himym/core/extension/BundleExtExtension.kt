package com.topping.core.extension

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

// getParcelableExtraCompat
@Suppress("UNCHECKED_CAST", "DEPRECATION")
inline fun <reified T : Parcelable> Bundle.getParcelableExtraCompat(name: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(name, T::class.java)
    } else {
        getParcelable(name) as? T  // 抑制弃用警告
    }
}

// getSerializableExtraCompat
@Suppress("UNCHECKED_CAST", "DEPRECATION")
inline fun <reified T : Serializable> Bundle.getSerializableExtraCompat(name: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(name, T::class.java)
    } else {
        getSerializable(name) as? T  // 抑制弃用警告
    }
}