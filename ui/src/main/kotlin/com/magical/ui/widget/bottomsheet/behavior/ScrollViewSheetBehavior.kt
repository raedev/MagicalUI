package com.magical.ui.widget.bottomsheet.behavior

import android.content.Context
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import com.google.android.material.bottomsheet.MagicalBottomSheetBehavior

/**
 * 嵌套ScrollView的行为
 * @author RAE
 * @date 2022/10/10
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class ScrollViewSheetBehavior(context: Context) :
    MagicalBottomSheetBehavior(context, null) {

    private var scrollView: ScrollView? = null

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: LinearLayout,
        layoutDirection: Int
    ): Boolean {
        val layout = child.getChildAt(0) as ViewGroup
        this.scrollView = layout.children.find { it is ScrollView } as ScrollView?
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: LinearLayout,
        event: MotionEvent
    ): Boolean {
        if (scrollView?.canScrollVertically(-1) == true) return false
        return super.onInterceptTouchEvent(parent, child, event)
    }
}