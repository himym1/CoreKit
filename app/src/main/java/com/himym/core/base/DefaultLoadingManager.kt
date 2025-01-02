package com.himym.core.base

import androidx.fragment.app.DialogFragment
import com.himym.core.dialog.LoadingFragment

/**
 *
 * @author: wangjianguo
 * @date: 2024/12/3
 * @desc:
 */
class DefaultLoadingManager(
    private val host: LoadingHost,
    private val dialogTag: String = "loading_fragment"
) : LoadingManager {

    override fun showLoading() {
        if (!host.isHostActive) return
        if (host.fragmentManager.findFragmentByTag(dialogTag) == null) {
            LoadingFragment().show(host.fragmentManager, dialogTag)
        }
    }

    override fun hideLoading() {
        val loadingFragment = host.fragmentManager.findFragmentByTag(dialogTag) as? DialogFragment
        loadingFragment?.let {
            host.fragmentManager.beginTransaction().remove(it).commitAllowingStateLoss()
        }
    }
}