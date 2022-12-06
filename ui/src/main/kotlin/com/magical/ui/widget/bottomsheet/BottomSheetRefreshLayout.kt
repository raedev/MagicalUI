package com.magical.ui.widget.bottomsheet

import android.content.Context
import android.os.Vibrator
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.MagicalBottomSheetBehavior
import com.magical.ui.widget.bottomsheet.behavior.RefreshNestedRecycleSheetBehavior
import java.lang.reflect.Field

/**
 * 滑动布局的下拉刷新，主要是解决BottomSheet和下拉刷新冲突问题。实现下拉到一定位置后回弹到BottomSheet的状态。
 * @author RAE
 * @date 2022/10/10
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class BottomSheetRefreshLayout(context: Context, attrs: AttributeSet?) :
    SwipeRefreshLayout(context, attrs) {

    private val totalUnconsumedField: Field =
        this::class.java.superclass.getDeclaredField("mTotalUnconsumed")

    private val currentTopField: Field =
        this::class.java.superclass.getDeclaredField("mTotalDragDistance")

    private var bottomSheetLayout: MagicalBottomSheetLayout? = null

    private val vibrator: Vibrator
    private var circleView: View? = null
    private var lastCanDragToNextState: Boolean = false

    /** 是否启用震动反馈 */
    var enableVibrator: Boolean = true

    init {
        totalUnconsumedField.isAccessible = true
        currentTopField.isAccessible = true
        this.vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    private fun getTotalUnconsumed(): Float {
        return totalUnconsumedField.getFloat(this)
    }

    private fun getCurrentTargetOffsetTop(): Float {
        return currentTopField.getFloat(this)
    }

    internal fun attachBottomSheetLayout(
        layout: MagicalBottomSheetLayout,
        recyclerView: RecyclerView
    ) {
        bottomSheetLayout = layout
        layout.behavior =
            RefreshNestedRecycleSheetBehavior(context, this, recyclerView)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        bottomSheetLayout = null
    }

    override fun addView(child: View?) {
        if (childCount <= 0) {
            this.circleView = child
        }
        super.addView(child)
    }


    private fun canDragToNextState(): Boolean {
        val behavior = bottomSheetLayout?.behavior ?: return false
        val totalUnconsumed = getTotalUnconsumed()
        val offset = getCurrentTargetOffsetTop()
        return behavior.state == BottomSheetBehavior.STATE_EXPANDED && totalUnconsumed > 0 && offset > 0 && totalUnconsumed > offset * 3
    }

    override fun onStopNestedScroll(target: View) {
        val behavior = bottomSheetLayout?.behavior ?: return super.onStopNestedScroll(target)
        // 实现下拉回弹
        if (canDragToNextState()) {
            onDragToNextState(behavior)
            stopNestedScroll()
            return
        }
        super.onStopNestedScroll(target)
    }

    private fun onDragToNextState(behavior: MagicalBottomSheetBehavior) {
        isRefreshing = false
        behavior.nextExpandedState()
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        super.onNestedScroll(
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )
        val view = circleView ?: return
        val canDragToNextState = canDragToNextState()
        view.visibility = if (canDragToNextState) View.GONE else View.VISIBLE
        if (canDragToNextState != lastCanDragToNextState) {
            lastCanDragToNextState = canDragToNextState
            // 震动反馈
            if (canDragToNextState && enableVibrator) this.vibrator.vibrate(80)
        }
    }

}