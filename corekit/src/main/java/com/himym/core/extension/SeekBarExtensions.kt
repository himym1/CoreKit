package com.topping.core.extension

import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar

/**
 * @author himym.
 * @description SeekBar 扩展方法
 */

inline fun AppCompatSeekBar.onProgressChanged(crossinline action: (SeekBar?, Int, Boolean) -> Unit) =
    addSeekBarChangeListener(onProgressChange = action)

inline fun AppCompatSeekBar.onStartTracking(crossinline action: (SeekBar?) -> Unit) =
    addSeekBarChangeListener(onStartTrackingTouch = action)

inline fun AppCompatSeekBar.onStopTracking(crossinline action: (SeekBar?) -> Unit) =
    addSeekBarChangeListener(onStopTrackingTouch = action)

/**
 * 添加 SeekBar 的监听器。
 *
 * @param onProgressChange 进度变化时触发的回调，默认不处理。
 * @param onStartTrackingTouch 开始拖动时触发的回调，默认不处理。
 * @param onStopTrackingTouch 停止拖动时触发的回调，默认不处理。
 */
inline fun AppCompatSeekBar.addSeekBarChangeListener(
    crossinline onProgressChange: (SeekBar?, Int, Boolean) -> Unit = { _, _, _ -> },
    crossinline onStartTrackingTouch: (SeekBar?) -> Unit = {},
    crossinline onStopTrackingTouch: (SeekBar?) -> Unit = {}
) {
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            onProgressChange(seekBar, progress, fromUser)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            onStartTrackingTouch(seekBar)
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            onStopTrackingTouch(seekBar)
        }
    })
}