package com.raedev.demo.bottomsheet

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.raedev.demo.R
import com.raedev.demo.databinding.ActivityDemoBottomSheetRvBinding
import kotlinx.coroutines.delay

/**
 * RecycleView + RefreshLayout BottomSheet 示例
 * @author RAE
 * @date 2023/02/19
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class RecycleViewBottomSheetActivity : BaseBottomSheetActivity() {

    override val layoutId: Int
        get() = R.layout.activity_demo_bottom_sheet_rv
    lateinit var binding: ActivityDemoBottomSheetRvBinding
    override fun onContentViewCreated(view: View) {
        binding = ActivityDemoBottomSheetRvBinding.bind(view)
        val behavior = binding.bottomSheet.behavior
        behavior.stateToCollapsed()
        binding.refresh.setOnRefreshListener {
            onRefresh()
        }
    }

    private fun onRefresh() {
        lifecycleScope.launchWhenCreated {
            delay(1000)
            binding.refresh.isRefreshing = false
        }
    }
}