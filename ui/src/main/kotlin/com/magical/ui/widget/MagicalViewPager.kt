package com.magical.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import com.magical.ui.R

/**
 * ViewPager
 * @author RAE
 * @date 2022/08/10
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class MagicalViewPager(context: Context, attrs: AttributeSet?) :
    ViewPager(context, attrs) {

    /**
     * 是否固定不滑动
     */
    var fixed: Boolean = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs)

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.MagicalViewPager)
        for (i in 0 until typedArray.indexCount) {
            when (val index = typedArray.getIndex(i)) {
                R.styleable.MagicalViewPager_mg_fixed -> this.fixed =
                    typedArray.getBoolean(index, false)
            }
        }
        typedArray.recycle()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (fixed) false else super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return if (fixed) false else super.onTouchEvent(ev)
    }


    override fun setCurrentItem(item: Int) {
        if (fixed) {
            super.setCurrentItem(item, false)
            return
        }
        super.setCurrentItem(item)
    }
}