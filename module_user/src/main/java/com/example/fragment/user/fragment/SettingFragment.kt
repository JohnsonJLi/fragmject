package com.example.fragment.user.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.fragment.library.base.utils.CacheUtils
import com.example.fragment.library.common.bean.UserBean
import com.example.fragment.library.common.constant.Keys
import com.example.fragment.library.common.constant.Router
import com.example.fragment.library.common.dialog.StandardDialog
import com.example.fragment.library.common.fragment.ViewModelFragment
import com.example.fragment.library.common.utils.WanHelper
import com.example.fragment.module.user.databinding.FragmentSettingBinding
import com.example.fragment.user.model.UserModel

class SettingFragment : ViewModelFragment<FragmentSettingBinding, UserModel>() {

    override fun setViewBinding(inflater: LayoutInflater): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        update()
    }

    private fun setupView() {
        binding.black.setOnClickListener { baseActivity.onBackPressed() }
        binding.systemTheme.setOnCheckedChangeListener { view, isChecked ->
            WanHelper.setUIMode(if (isChecked) -1 else 1)
            view.postDelayed({
                baseActivity.initUIMode()
            }, 300)
        }
        binding.darkTheme.setOnCheckedChangeListener { view, isChecked ->
            WanHelper.setUIMode(if (isChecked) 2 else 1)
            view.postDelayed({
                baseActivity.initUIMode()
            }, 300)
        }
        binding.cacheSize.text = CacheUtils.getTotalCacheSize(baseActivity)
        binding.clearCache.setOnClickListener {
            StandardDialog.newInstance()
                .setContent("确定要清除缓存吗？")
                .setOnDialogClickListener(object : StandardDialog.OnDialogClickListener {
                    override fun onConfirm(dialog: StandardDialog) {
                        CacheUtils.clearAllCache(baseActivity)
                        binding.cacheSize.text = CacheUtils.getTotalCacheSize(baseActivity)
                    }

                    override fun onCancel(dialog: StandardDialog) {
                    }
                })
                .show(childFragmentManager)
        }
        binding.update.setOnClickListener {
            StandardDialog.newInstance()
                .setTitle("感谢使用")
                .setContent("作者不知道跑哪里去了，可能再也不会更新了")
                .setOnDialogClickListener(object : StandardDialog.OnDialogClickListener {
                    override fun onConfirm(dialog: StandardDialog) {
                    }

                    override fun onCancel(dialog: StandardDialog) {
                    }
                })
                .show(childFragmentManager)
        }
        binding.about.setOnClickListener {
            val args = Bundle()
            args.putString(Keys.URL, "https://wanandroid.com")
            baseActivity.navigation(Router.WEB, args)
        }
        binding.privacyPolicy.setOnClickListener {
            val args = Bundle()
            args.putString(Keys.URL, "file:///android_asset/privacy_policy.html")
            baseActivity.navigation(Router.WEB, args)
        }
        binding.feedback.setOnClickListener {
            val args = Bundle()
            args.putString(Keys.URL, "https://github.com/miaowmiaow/FragmentProject/issues")
            baseActivity.navigation(Router.WEB, args)
        }
        binding.logout.setOnClickListener {
            StandardDialog.newInstance()
                .setContent("确定退出登录吗？")
                .setOnDialogClickListener(object : StandardDialog.OnDialogClickListener {
                    override fun onConfirm(dialog: StandardDialog) {
                        viewModel.logout()
                    }

                    override fun onCancel(dialog: StandardDialog) {
                    }
                })
                .show(childFragmentManager)
        }
    }

    private fun update() {
        WanHelper.getUIMode().observe(viewLifecycleOwner, {
            when (it) {
                1 -> {
                    binding.systemTheme.isChecked = false
                    binding.systemTheme.isEnabled = true
                    binding.darkTheme.isChecked = false
                    binding.darkTheme.isEnabled = true
                }
                2 -> {
                    binding.systemTheme.isChecked = false
                    binding.systemTheme.isEnabled = false
                    binding.darkTheme.isChecked = true
                    binding.darkTheme.isEnabled = true
                }
                else -> {
                    binding.systemTheme.isChecked = true
                    binding.systemTheme.isEnabled = true
                    binding.darkTheme.isChecked = false
                    binding.darkTheme.isEnabled = false
                }
            }
        })
        viewModel.logoutResult.observe(viewLifecycleOwner, {
            if (it.errorCode == "0") {
                WanHelper.setUser(UserBean())
                baseActivity.onBackPressed()
            }
        })
    }

}