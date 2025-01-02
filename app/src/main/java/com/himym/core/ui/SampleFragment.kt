package com.topping.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.topping.core.entity.User
import com.topping.core.extension.arg
import com.topping.core.extension.viewBinding
import com.topping.core.extension.withArgs
import com.topping.core.helper.ePrint
import com.himym.main.R
import com.himym.main.databinding.FragmentSampleBinding

/**
 *
 * @author: wangjianguo
 * @date: 2024/11/29
 * @desc:
 */
class SampleFragment :BaseFragment<FragmentSampleBinding>(){

    private val name: String by arg(KEY_NAME, "Default Name")
    private val age: Int by arg(KEY_AGE, -1)
    private val user: User? by arg(KEY_USER, User(0, "Default User"))
    private val users: List<User> by arg(KEY_USERS, emptyList())
    private val user1: List<User> by arg(KEY_USERS, emptyList())

    companion object {
        private const val KEY_NAME = "name"
        private const val KEY_AGE = "age"
        private const val KEY_USER = "user"
        private const val KEY_USERS = "users"

        fun newInstance(
            name: String,
            age: Int,
            user: User?,
            users: List<User>
        ) = SampleFragment().withArgs(
            KEY_NAME to name,
            KEY_AGE to age,
            KEY_USER to user,
            KEY_USERS to users
        )
    }

    override fun initFragment(view: View, savedInstanceState: Bundle?) {
        ePrint {
           "name: $name, age: $age, user: $user, users: $users"
        }

    }
}