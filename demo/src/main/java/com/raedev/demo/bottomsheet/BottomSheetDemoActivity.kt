package com.raedev.demo.bottomsheet

import android.content.Intent
import android.os.Bundle
import com.raedev.demo.BaseActivity
import com.raedev.demo.databinding.ActivityDemoBottomSheetBinding
import kotlin.reflect.KClass

/**
 * BottomSheet示例入口
 * @author RAE
 * @date 2023/02/19
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class BottomSheetDemoActivity : BaseActivity() {

    override var homeAsUpEnabled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDemoBottomSheetBinding
        setContentView(ActivityDemoBottomSheetBinding.inflate(layoutInflater).also {
            binding = it
        }.root)

        binding.btnDemo1.setOnClickListener { startActivity(BasicBottomSheetActivity::class) }
        binding.btnDemo2.setOnClickListener { startActivity(RecycleViewBottomSheetActivity::class) }
    }

    private fun startActivity(clazz: KClass<*>) {
        startActivity(Intent(this, clazz.java))
    }
}