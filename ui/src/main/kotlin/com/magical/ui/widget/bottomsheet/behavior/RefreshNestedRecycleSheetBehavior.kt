package com.magical.ui.widget.bottomsheet.behavior

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.MagicalBottomSheetBehavior
import com.magical.ui.widget.bottomsheet.BottomSheetRefreshLayout
import com.magical.ui.widget.bottomsheet.MagicalBottomSheetLayout

/**
 * RefreshLayout 嵌套RecycleView的行为
 * @author RAE
 * @date 2022/10/10
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class RefreshNestedRecycleSheetBehavior(
    context: Context,
    val refreshLayout: SwipeRefreshLayout,
    val recyclerView: RecyclerView,
) :
    MagicalBottomSheetBehavior(context, null) {

//    private var refreshLayout: SwipeRefreshLayout? = null
//    private var recyclerView: RecyclerView? = null

//    override fun onLayoutChild(
//        parent: CoordinatorLayout,
//        child: LinearLayout,
//        layoutDirection: Int
//    ): Boolean {
//        // 找View
//        if (refreshLayout == null) {
//            val layout = child.getChildAt(0) as ViewGroup
//            layout.children.forEach {
//                if (it is SwipeRefreshLayout) {
//                    refreshLayout = it
//                    recyclerView = it.children.find { v -> v is RecyclerView } as RecyclerView?
//                    updateRefreshLayoutState()
//                }
//            }
//        }
//        refreshLayout ?: throw NullPointerException("没有找到下拉刷新布局")
//        recyclerView ?: throw NullPointerException("没有找到RecyclerView布局")
//        return super.onLayoutChild(parent, child, layoutDirection)
//    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: LinearLayout,
        layoutDirection: Int
    ): Boolean {
        if (refreshLayout is BottomSheetRefreshLayout) {
            refreshLayout.attachBottomSheetLayout(parent as MagicalBottomSheetLayout, recyclerView)
        }
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    private fun updateRefreshLayoutState() {
        val layout = refreshLayout
        val enable = state == STATE_EXPANDED
        if (layout.isEnabled != enable) {
            layout.isEnabled = enable
        }
        Log.d("rae", "updateRefreshLayoutState: ${layout.isEnabled}")
    }

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: LinearLayout,
        event: MotionEvent
    ): Boolean {
        // 非展开状态都拦截，保留点击事件
        if (state != STATE_EXPANDED && event.action == MotionEvent.ACTION_MOVE) {
            return true
        }
        return super.onInterceptTouchEvent(parent, child, event)
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: LinearLayout,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        // 在展开状态下启用下拉刷新
        return super.onStartNestedScroll(
            coordinatorLayout,
            child,
            directTargetChild,
            target,
            axes,
            type
        ) && state != STATE_EXPANDED
    }

    override fun onStateChanged(state: Int) {
        super.onStateChanged(state)
        updateRefreshLayoutState()
    }

}