package com.magical.ui.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import com.magical.ui.R

/**
 * TabLayout
 * @author RAE
 * @date 2022/08/07
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class MagicalTabLayout(context: Context, attrs: AttributeSet?) :
    TabLayout(context, attrs) {

    constructor(context: Context) : this(context, null)

    private var tabMarginEnd: Float = -1F

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MagicalTabLayout)
        for (i in 0 until typedArray.indexCount) {
            when (val index = typedArray.getIndex(i)) {
                R.styleable.MagicalTabLayout_tabMarginEnd -> {
                    tabMarginEnd = typedArray.getDimension(index, -1F)
                }
            }
        }
        typedArray.recycle()
    }

    override fun selectTab(tab: Tab?, updateIndicator: Boolean) {
        super.selectTab(tab, updateIndicator)
        tab ?: return
        // 选中粗体
        for (i in 0 until this.tabCount) {
            val itemTab = getTabAt(i) ?: continue
            if (itemTab.view.childCount < 2) continue
            val textView = itemTab.view.getChildAt(1) as TextView? ?: continue
            val value = if (i == tab.position) Typeface.BOLD else Typeface.NORMAL
            when (textView.typeface) {
                null -> textView.setTypeface(null, value)
                else -> textView.typeface = Typeface.create(textView.typeface, value)
            }
        }
    }

    override fun addTab(tab: Tab, position: Int, setSelected: Boolean) {
        super.addTab(tab, position, setSelected)
        if (tabMarginEnd != -1F) {
            (tab.view.layoutParams as MarginLayoutParams).marginEnd = tabMarginEnd.toInt()
        }
    }
}