@file:Suppress("MemberVisibilityCanBePrivate")

package com.magical.ui.behavior

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * 底部滑动框行为，默认高度比例：peekHeightRatio(0.3)-> halfExpandedRatio(0.7) -> Expanded(1)
 * @author RAE
 * @date 2023/02/18
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class MagicalBottomSheetBehavior<V : View>(context: Context, attr: AttributeSet? = null) :
    BottomSheetBehavior<V>(context, attr) {

    private var _parentLayout: CoordinatorLayout? = null

    /** 是否启动三段式布局 */
    var enable: Boolean = false
        set(value) {
            field = value
            updateEnableState(value)
        }

    /** PeekHeight 比例 */
    var peekHeightRatio: Float = 0.3F
        set(value) {
            field = value
            if (enable && value > 0 && value < 1F) {
                updatePeekHeightByRatio()
            }
        }


    init {
        // 默认启用
        this.enable = true
        // 默认是半展开状态
        this.state = STATE_HALF_EXPANDED
    }

    protected open fun updateEnableState(value: Boolean) {
        this.isFitToContents = !value
        if (!value) return
        // 启用模式
        if (this.halfExpandedRatio == 0.5f) {
            this.halfExpandedRatio = 0.7f
        }
    }

    /**
     * 更新PeekHeight高度
     */
    internal open fun updatePeekHeightByRatio() {
        if (enable && peekHeightRatio != -1f && _parentLayout != null) {
            peekHeight = (_parentLayout!!.measuredHeight * peekHeightRatio).toInt()
        }
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: V, layoutDirection: Int): Boolean {
        if (_parentLayout == null) {
            this._parentLayout = parent
            updatePeekHeightByRatio()
        }
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        Log.d("rae.behavior", "onStartNestedScroll：${target.tag}")
        if (target is SwipeRefreshLayout) {
            return canStartNestedScroll(target)
        }
        return super.onStartNestedScroll(
            coordinatorLayout, child, directTargetChild, target, axes, type
        )
    }

    /**
     * 下拉刷新布局是否可以嵌套滚动
     */
    private fun canStartNestedScroll(target: SwipeRefreshLayout): Boolean {
        // 只有在完全展开的情况下才能进行下拉刷新
        Log.d("rae.behavior", "${System.currentTimeMillis()}下拉刷新滚动：${target.tag}")
        target.setSlingshotDistance((target.progressViewEndOffset * 0.5).toInt())
        if (state != STATE_EXPANDED) return true
        return false
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (target is SwipeRefreshLayout && !canStartNestedScroll(target)) {
            // 不允许嵌套滚动
            target.stopNestedScroll()
            return
        }
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        type: Int
    ) {
        if (state == STATE_SETTLING) return
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
    }


    /**
     * 隐藏
     */
    fun stateToHide() {
        if (isHideable) state = STATE_HIDDEN
    }

    /**
     * 完全展开
     */
    fun stateToExpanded() {
        state = STATE_EXPANDED
    }

    /**
     * 折叠（Peek状态）
     */
    fun stateToCollapsed() {
        state = STATE_COLLAPSED
    }

    /**
     * 半展开状态也是中间断状态
     */
    fun stateToHalfExpanded() {
        if (enable) state = STATE_HALF_EXPANDED
    }

}