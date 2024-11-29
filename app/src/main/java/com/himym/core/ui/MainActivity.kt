package com.himym.core.ui

import android.os.Bundle
import com.himym.core.anno.ActivityConfig
import com.himym.core.base.BaseCommonActivity
import com.himym.core.entity.User
import com.himym.core.entity.User1
import com.himym.core.extension.setOnDebounceClickListener
import com.himym.main.R
import com.himym.main.databinding.ActivityMainBinding

/**
 *
 * @author: wangjianguo
 * @date: 2024/11/28
 * @desc: 主页
 */
@ActivityConfig(hideStatusBar = true)
class MainActivity:BaseCommonActivity<ActivityMainBinding>() {

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initActivity(savedInstanceState: Bundle?) {
        mBinding.btn.setOnDebounceClickListener {
            SecondActivity.start(
                this,
                "himym",
                18,
                User(1, "himym"),
                listOf(User(1, "himym")),
                listOf(User1("himym", sex = "11"))
            )
        }
    }
}