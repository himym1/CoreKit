package com.himym.core.ui

import android.content.Context
import android.os.Bundle
import com.himym.core.anno.ActivityConfig
import com.himym.core.base.BaseCommonActivity
import com.himym.core.base.toolbar.ToolbarState
import com.himym.core.base.toolbar.toolbar
import com.himym.core.entity.User
import com.himym.core.entity.User1
import com.himym.core.extension.intentExtra
import com.himym.core.extension.launchActivity
import com.himym.core.extension.toast
import com.himym.core.helper.ePrint
import com.himym.main.R
import com.himym.main.databinding.ActivitySecondBinding

/**
 *
 * @author: wangjianguo
 * @date: 2024/11/29
 * @desc:
 */
@ActivityConfig(hideStatusBar = true)
class SecondActivity : BaseCommonActivity<ActivitySecondBinding>() {

    private val name by intentExtra<String>(KEY_NAME, "Default Name")
    private val age by intentExtra<Int>(KEY_AGE, -1)
    private val user by intentExtra<User>(KEY_USER, User(0, "Default User"))
    private val users by intentExtra<List<User>>(KEY_USERS, emptyList())
    private val user1 by intentExtra<List<User1>>(KEY_USERS1, emptyList())

    companion object {
        const val KEY_NAME = "name"
        const val KEY_AGE = "age"
        const val KEY_USER = "user"
        const val KEY_USERS = "users"
        const val KEY_USERS1 = "users1"

        fun start(
            context: Context,
            name: String,
            age: Int,
            user: User,
            users: List<User>,
            user1: List<User1>
        ) {
            context.launchActivity<SecondActivity>(
                KEY_NAME to name,
                KEY_AGE to age,
                KEY_USER to user,
                KEY_USERS to users,
                KEY_USERS1 to user1
            )
        }
    }

    override fun initActivity(savedInstanceState: Bundle?) {
        ePrint {
            "name: $name, age: $age user: $user users: $users user1: $user1"
        }

        val fragment = SampleFragment.newInstance(
            name = "John Doe",
            age = 30,
            user = User(1, "John"),
            users = listOf(User(1, "John"), User(2, "Doe"))
        )

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun getInitialToolbarState(): ToolbarState? {
        return toolbar {
            title = "Second Activity"
            onBackClick = {
                "点击了".toast()
            }
        }
    }
}