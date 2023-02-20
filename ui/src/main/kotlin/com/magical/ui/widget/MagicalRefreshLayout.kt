@file:Suppress("MemberVisibilityCanBePrivate")

package com.magical.ui.widget

import android.content.Context
import android.os.Vibrator
import android.util.AttributeSet
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HALF_EXPANDED
import java.lang.reflect.Field

/**
 * 下拉刷新布局
 * @author RAE
 * @date 2023/02/18
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class MagicalRefreshLayout(context: Context, attrs: AttributeSet?) :
    SwipeRefreshLayout(context, attrs) {

    private val vibrator: Vibrator
    private var circleView: View? = null
    private var lastCanDragToNextState: Boolean = false

    /** 是否启用震动反馈 */
    var enableVibrator: Boolean = false

    private val totalUnconsumedField: Field =
        this::class.java.superclass.getDeclaredField("mTotalUnconsumed")

    private val currentTopField: Field =
        this::class.java.superclass.getDeclaredField("mTotalDragDistance")

    private var nestedScrollingParent: MagicalBottomSheetLayout? = null


    init {
        this.vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        totalUnconsumedField.isAccessible = true
        currentTopField.isAccessible = true
    }

    private fun getTotalUnconsumed(): Float {
        return totalUnconsumedField.getFloat(this)
    }

    private fun getCurrentTargetOffsetTop(): Float {
        return currentTopField.getFloat(this)
    }

    override fun addView(child: View?) {
        if (childCount <= 0) {
            this.circleView = child
        }
        super.addView(child)
    }

    private fun canDragToNextState(): Boolean {
        if (nestedScrollingParent?.behavior?.enable == false) return false
        val totalUnconsumed = getTotalUnconsumed()
        val offset = getCurrentTargetOffsetTop()
        return totalUnconsumed > 0 && offset > 0 && totalUnconsumed > offset * 3
    }


    override fun onStopNestedScroll(target: View) {
        // 实现下拉回弹
        if (!canDragToNextState()) {
            return super.onStopNestedScroll(target)
        }
        super.onStopNestedScroll(this)
        isRefreshing = false
        val parent = nestedScrollingParent
        // 触发回弹
        if (parent != null && parent.behavior.state == STATE_EXPANDED) {
            parent.behavior.state = STATE_HALF_EXPANDED
            return
        }
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        if (nestedScrollingParent == null) {
            nestedScrollingParent = MagicalBottomSheetLayout.findCurrentAsParent(this)
        }
        return super.onStartNestedScroll(child, target, axes, type)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int,
        type: Int, consumed: IntArray
    ) {
        super.onNestedScroll(
            target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed
        )
        val canDragToNextState = canDragToNextState()
        if (canDragToNextState != lastCanDragToNextState) {
            lastCanDragToNextState = canDragToNextState
            // 震动反馈
            if (canDragToNextState && enableVibrator) this.vibrator.vibrate(60)
        }
    }

}