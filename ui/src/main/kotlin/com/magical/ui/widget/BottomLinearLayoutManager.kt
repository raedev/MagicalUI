package com.magical.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * 配合底部MagicalBottomLayout的布局
 * 主要用于：滑动Rv的时候判断Touch事件拦截
 * @author RAE
 * @date 2022/08/19
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class BottomLinearLayoutManager : LinearLayoutManager {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


//    override fun canScrollVertically(): Boolean {
//        var position = findFirstCompletelyVisibleItemPosition()
//        if (position <= 1) return true
//        position = if (position == -1) findFirstVisibleItemPosition() else position
//        if (position == 0) return true
//
//        return super.canScrollVertically()
//    }
}