package com.raedev.demo.bottomsheet

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.raedev.demo.BaseActivity
import com.raedev.demo.R
import com.raedev.demo.databinding.ActivityDemoBottomSheetTemplateBinding

/**
 * BottomSheet 公共模板
 * @author RAE
 * @date 2023/02/19
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class BaseBottomSheetActivity : BaseActivity() {

    abstract val layoutId: Int
    private lateinit var binding: ActivityDemoBottomSheetTemplateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoBottomSheetTemplateBinding.inflate(layoutInflater)
        binding.stub.layoutResource = layoutId
        val view = binding.stub.inflate()
        onContentViewCreated(view)
        setContentView(binding.root)
        binding.video.apply {
            setVideoURI(
                Uri.parse("android.resource://$packageName/${R.raw.demo}")
            )
            start()
            setOnCompletionListener { start() }
        }
    }

    fun addAction(block: () -> Unit) {
    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding.video.isPlaying) {
            binding.video.stopPlayback()
        }
    }

    abstract fun onContentViewCreated(view: View)
}