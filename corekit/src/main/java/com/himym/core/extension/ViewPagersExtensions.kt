package com.topping.core.extension

import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * @author himym.
 * @description ViewPager 扩展方法
 */

fun ViewPager.onPageSelected(action: (Int) -> Unit) =
    addPageChangeListener(onPageSelected = action)

fun ViewPager.onPageScrolled(action: (Int, Float, Int) -> Unit) =
    addPageChangeListener(onPageScrolled = action)

fun ViewPager.onScrollStateChanged(action: (Int) -> Unit) =
    addPageChangeListener(onPageScrollStateChanged = action)

private fun ViewPager.addPageChangeListener(
    onPageScrolled: (Int, Float, Int) -> Unit = { _, _, _ -> },
    onPageSelected: (Int) -> Unit = {},
    onPageScrollStateChanged: (Int) -> Unit = {}
): ViewPager.OnPageChangeListener {
    val listener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, offset: Float, offsetPixels: Int) {
            onPageScrolled(position, offset, offsetPixels)
        }

        override fun onPageSelected(position: Int) {
            onPageSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            onPageScrollStateChanged(state)
        }
    }
    addOnPageChangeListener(listener)
    return listener
}

// ViewPager2 Extensions
fun ViewPager2.bindWithTabLayout(
    tabLayout: TabLayout,
    bind: (TabLayout.Tab, Int) -> Unit
) = TabLayoutMediator(tabLayout, this) { tab, position -> bind(tab, position) }.attach()

fun ViewPager2.onPageSelected(action: (Int) -> Unit) =
    registerPageChangeCallback(onPageSelected = action)

fun ViewPager2.onPageScrolled(action: (Int, Float, Int) -> Unit) =
    registerPageChangeCallback(onPageScrolled = action)

fun ViewPager2.onScrollStateChanged(action: (Int) -> Unit) =
    registerPageChangeCallback(onPageScrollStateChanged = action)

private fun ViewPager2.registerPageChangeCallback(
    onPageScrolled: (Int, Float, Int) -> Unit = { _, _, _ -> },
    onPageSelected: (Int) -> Unit = {},
    onPageScrollStateChanged: (Int) -> Unit = {}
): ViewPager2.OnPageChangeCallback {
    val callback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(position: Int, offset: Float, offsetPixels: Int) {
            onPageScrolled(position, offset, offsetPixels)
        }

        override fun onPageSelected(position: Int) {
            onPageSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            onPageScrollStateChanged(state)
        }
    }
    registerOnPageChangeCallback(callback)
    return callback
}