package com.topping.core.ui

import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.topping.core.anno.ActivityConfig
import com.topping.core.base.BaseCommonActivity
import com.topping.core.base.toolbar.ToolbarState
import com.topping.core.entity.User
import com.topping.core.entity.User1
import com.topping.core.extension.loadImage
import com.topping.core.extension.setOnDebounceClickListener
import com.topping.core.extension.showCustomListDialog
import com.topping.core.extension.showSingleChoiceDialog
import com.topping.core.extension.toast
import com.topping.core.helper.ePrint
import com.himym.main.R
import com.himym.main.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

/**
 *
 * @author: wangjianguo
 * @date: 2024/11/28
 * @desc: 主页
 */
@ActivityConfig(hideStatusBar = true)
class MainActivity:BaseCommonActivity<ActivityMainBinding>() {

    private val viewModel: MainViewModel by viewModels()

    override fun initView() {
        super.initView()
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

        mBinding.btnDialog.setOnDebounceClickListener {
            showSingleChoiceDialog(
                "请选择",
                listOf("选项1", "选项2", "选项3")
            ) { index, option ->
                toast("选择了$option")
            }
        }

        mBinding.btnDialogList.setOnDebounceClickListener {
            showCustomListDialog(  // 明确指定泛型类型为 User
                title = "选择用户",
                items = listOf(
                    User(1, "用户1", "https://s1.aigei.com/src/img/jpg/18/18f6db8f29004415a7d881a2863a5943.jpg?imageMogr2/auto-orient/thumbnail/!282x282r/gravity/Center/crop/282x282/quality/85/%7CimageView2/2/w/282&e=1735488000&token=P7S2Xpzfz11vAkASLTkfHN7Fw-oOZBecqeJaxypL:8RFf7zPPLGz6XcZN6W5dzKSHrxk="),
                    User(2, "用户2", "https://s1.aigei.com/src/img/jpg/5f/5f47f1aeb99c45ba8e0be2c40a6bee2b.jpg?imageMogr2/auto-orient/thumbnail/!282x282r/gravity/Center/crop/282x282/quality/85/%7CimageView2/2/w/282&e=1735488000&token=P7S2Xpzfz11vAkASLTkfHN7Fw-oOZBecqeJaxypL:6NcnzfIFhwOpyH30SyQwwHQLIDE=")
                ),
                itemLayoutRes = R.layout.item_user,
                bindView = { itemView, user, position ->  // 使用具名参数
                    itemView.apply {
                        findViewById<TextView>(R.id.tv_name).text = user.name
                        findViewById<ImageView>(R.id.iv_avatar).loadImage(user.avatar)
                    }
                },
                onItemSelected = { index, user ->  // 使用具名参数
                    Toast.makeText(this, "选择了用户：${user.name}", Toast.LENGTH_SHORT).show()
                }
            )
        }

    }

    override fun getInitialToolbarState(): ToolbarState = ToolbarState.WithSwitch(
        title = "主页",
        rightText = "独立均衡",
        isChecked = false,
        onSwitchChanged = { isChecked ->
            viewModel.loadSwitchState(isChecked)
        }
    ).apply {
        ePrint {
            "title: $title, rightText: $rightText, isChecked: $isChecked"
        }
        viewModel.setToolbarReady()
    }

    override fun initData() {
        super.initData()
        // 这里的更新会通过 toolbarState 的观察自动更新 UI
        viewModel.loadSwitchState(true).apply {
            ePrint {
                "isChecked: $this"
            }
        }
    }

    override fun initObserver() {
        lifecycleScope.launch {
            // 这里接收的状态已经确保是在 Toolbar 初始化完成后的
            viewModel.toolbarState.collect { state ->
                toolbarHelper.updateState { currentState ->
                    when (currentState) {
                        is ToolbarState.WithSwitch -> currentState.copy(isChecked = state)
                        else -> currentState
                    }
                }
            }
        }
    }
}