package com.himym.core.ui

import android.os.Bundle
import com.himym.main.R
import com.himym.main.databinding.ActivityMainBinding

/**
 *
 * @author: wangjianguo
 * @date: 2024/11/28
 * @desc: 主页
 */
class MainActivity:BaseActivity<ActivityMainBinding>() {

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initActivity(savedInstanceState: Bundle?) {

    }
}