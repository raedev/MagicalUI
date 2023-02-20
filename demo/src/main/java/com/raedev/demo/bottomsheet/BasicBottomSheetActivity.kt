package com.raedev.demo.bottomsheet

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.magical.ui.UiCompat
import com.raedev.demo.R
import com.raedev.demo.databinding.ActivityDemoBottomSheetBasicBinding

/**
 * 基础的BottomSheet示例
 * @author RAE
 * @date 2023/02/19
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class BasicBottomSheetActivity : BaseBottomSheetActivity() {

    override val layoutId: Int
        get() = R.layout.activity_demo_bottom_sheet_basic

    override fun onContentViewCreated(view: View) {
        val binding = ActivityDemoBottomSheetBasicBinding.bind(view)
        val behavior = binding.bottomSheet.behavior
        behavior.stateToCollapsed()
        binding.tvPeek.post {
            // Peek高度设置
            behavior.peekHeight = binding.llPeek.bottom
        }

        // 状态回调监听
        behavior.addBottomSheetCallback(object : BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.tvPeek.text = when (newState) {
                    STATE_HALF_EXPANDED -> "当前为中部位置"
                    STATE_COLLAPSED -> "当前为Peek位置，拉上来看看吧"
                    STATE_EXPANDED -> "当前为完全展开状态"
                    STATE_DRAGGING -> "拖拽中"
                    STATE_SETTLING -> "手指释放中"
                    else -> "当前状态：$newState"
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
        })


        binding.btnClickme.setOnClickListener {
            UiCompat.showToast(this, "点击事件触发了")
        }

        binding.checkboxBottomSheet.setOnCheckedChangeListener { _, isChecked ->
            behavior.enable = isChecked
        }

    }
}