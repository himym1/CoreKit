@file:Suppress("DEPRECATION")

package com.topping.core.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.topping.core.helper.KLogger

/**
 * @author himym.
 * @description adapter for ViewPager with fragment
 */
open class BaseFragmentPagerAdapter(
    fm: FragmentManager, fragments: MutableList<out Fragment>, titles: Array<String>? = null
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT), KLogger {

    private var mFragments = fragments
    private var mTitles = titles

    init {
        if (mTitles.isNullOrEmpty()) mTitles = Array(fragments.size) { "" }
    }

    override fun getItem(position: Int): Fragment = mFragments[position]

    override fun getCount(): Int = mFragments.size

    override fun getPageTitle(position: Int): CharSequence? =
        if (mTitles.isNullOrEmpty()) super.getPageTitle(position) else mTitles?.get(position) ?: ""
}