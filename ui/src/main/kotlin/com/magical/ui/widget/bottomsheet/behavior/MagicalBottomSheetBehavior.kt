@file:Suppress("PackageDirectoryMismatch")

// 包名和BottomSheetBehavior在同一下面使其能够访问内部方法。
package com.google.android.material.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.magical.ui.utils.MagicalUtils
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * BottomSheetBehavior 行为控制，主要作用：
 * - 1.控制三段式滑动，自动切换下一个状态
 * - 2.触摸事件拦截分发，处理和其他滑动View的冲突问题
 * - 3.三段式实现思路：增加一个中间状态，当View释放的时候，回弹到该状态。
 *
 * @author RAE
 * @date 2022/08/24
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class MagicalBottomSheetBehavior(context: Context, attrs: AttributeSet?) :
    BottomSheetBehavior<LinearLayout>(context, attrs) {

    companion object {
        /** 中间状态 */
        const val STATE_MIDDLE: Int = 0

        /** 按顺序排序 */
        val STATE_SORT_ARRAY =
            intArrayOf(STATE_HIDDEN, STATE_COLLAPSED, STATE_MIDDLE, STATE_EXPANDED)
    }

    /** 中间状态的可活动范围相对middleExpandedHeight高度的比例 */
    var middleRangeRatio = .3F

    /** 是否启用中间状态 */
    var isEnableMiddle = true

    /** 中间位置状态的高度 */
    val middleExpandedHeight: Float
        get() = parentHeight * .6f

    /** 上一个View的TOP距离 */
    private var lastChildTop: Float = 0F

    /**
     * 获取中间位置的Top距离
     * 公式 = 父布局高度 - 中间位置状态的高度
     */
    fun getMiddleTop(): Float {
        return parentHeight - middleExpandedHeight
    }


    /**
     * 拦截View回弹事件
     */
    override fun startSettlingAnimation(child: View, state: Int, top: Int, drag: Boolean) {
        Log.d("rae", "startSettlingAnimation: $state")
        if (!isEnableMiddle) return super.startSettlingAnimation(child, state, top, drag)
        var newTop = top
        var newState = state
        val currentTop = child.top.toFloat()
        val middleTop = getMiddleTop()
        val offset = currentTop - middleTop

        // 下一个状态
        val direction = if (lastChildTop - currentTop >= 0) 1 else -1
        val nextState = getNextState(this.lastStableState, direction)

        if (abs(offset) <= abs((middleExpandedHeight * middleRangeRatio))) {
            newTop = middleTop.toInt()
            newState = STATE_MIDDLE
        }

        MagicalUtils.debug("新的状态[${newState.formatState()}]， 上一个状态：${lastStableState.formatState()}， 下一个状态${nextState.formatState()}")

        // 越级限制，明确按照：折叠 -> 中间 -> 展开 -> 中间 -> 折叠 的顺序
        if (
        // 状态不一致
            newState != lastStableState && nextState != newState
            // 一拉到顶或底
            && child.top != collapsedOffset && child.top != fitToContentsOffset
        ) {
            newTop = when (nextState) {
                STATE_COLLAPSED -> collapsedOffset
                STATE_EXPANDED -> fitToContentsOffset
                STATE_MIDDLE -> middleTop.toInt()
                else -> top
            }
            newState = nextState
//            MagicalUtils.warn("状态不一致[${newState.formatState()}]！上一个状态：${lastStableState.formatState()}， 应该为：${nextState.formatState()}")
        }
        super.startSettlingAnimation(child, newState, newTop, drag)
        if (newState != state) {
            this.lastStableState = newState
        }
        this.lastChildTop = currentTop
    }

    /**
     * 下一个状态
     * @param state 当前状态
     * @param direction 滑动方向: -1向下，1向上
     */
    private fun getNextState(state: Int, direction: Int): Int {
        val next = STATE_SORT_ARRAY.indexOfFirst { it == state } + direction
        val index = max(0, min(next, STATE_SORT_ARRAY.size - 1))
        return STATE_SORT_ARRAY[index]
    }

    override fun setStateInternal(state: Int) {
        val oldState = this.state
        super.setStateInternal(state)
        if (oldState != state) this.onStateChanged(state)
    }

    /**
     * 状态发生改变
     * @param state 当前状态
     */
    protected open fun onStateChanged(state: Int) = Unit

    protected fun Int.formatState(): String {
        return when (this) {
            STATE_EXPANDED -> "展开"
            STATE_COLLAPSED -> "折叠"
            STATE_HALF_EXPANDED -> "HALF_EXPANDED"
            STATE_DRAGGING -> "DRAGGING"
            STATE_HIDDEN -> "隐藏"
            STATE_MIDDLE -> "中间"
            else -> this.toString()
        }
    }

    /**
     * 展开状态的下一个状态
     */
    @SuppressLint("WrongConstant")
    fun nextExpandedState() {
        val next = if (isEnableMiddle) STATE_MIDDLE else STATE_COLLAPSED
        this.setState(next)
    }

    @SuppressLint("WrongConstant")
    override fun settleToState(child: View, state: Int) {
        Log.d("rae", "settleToState: ${state.formatState()}")
        if (state == STATE_MIDDLE) {
            this.state = STATE_MIDDLE
            startSettlingAnimation(child, state, getMiddleTop().toInt(), false)
            return
        }
        super.settleToState(child, state)
    }
}