package com.magical.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.magical.ui.R

/**
 * 占位布局
 * @author RAE
 * @date 2022/08/26
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class MagicalHolderView : FrameLayout {

    private lateinit var loadingView: ImageView
    private lateinit var loadingTextView: TextView
    private lateinit var messageView: TextView
    private lateinit var retryView: TextView
    private lateinit var loadingLayout: ViewGroup
    private lateinit var emptyLayout: ViewGroup

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.mg_holder_layout, this, true)
        emptyLayout = findViewById(R.id.ll_empty)
        loadingLayout = findViewById(R.id.ll_loading)
        loadingView = findViewById(R.id.pb_loading)
        loadingTextView = findViewById(R.id.tv_loading)
        messageView = findViewById(R.id.tv_empty_message)
        retryView = findViewById(R.id.tv_retry_message)
        showLoading()
    }

    /**
     * 显示加载中
     */
    fun showLoading(message: String? = null) {
        loadingLayout.visibility = View.VISIBLE
        emptyLayout.visibility = View.GONE
        loadingView.drawable.setVisible(true, true)
    }


    /**
     * 显示空视图
     */
    fun showEmpty(message: String? = null) {
        loadingLayout.visibility = View.GONE
        emptyLayout.visibility = View.VISIBLE
        messageView.text = message ?: context.getString(R.string.mg_empty_message)
    }

}