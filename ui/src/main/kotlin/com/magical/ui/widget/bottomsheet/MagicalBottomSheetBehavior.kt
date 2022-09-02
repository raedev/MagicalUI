//package com.magical.ui.widget.bottomsheet

// 包名和BottomSheetBehavior在同一下面使其能够访问内部方法。
@file:Suppress("PackageDirectoryMismatch")

package com.google.android.material.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.magical.ui.utils.MagicalUtils

/**
 * BottomSheetBehavior 行为控制。
 * 主要作用：
 * 1、控制三段式滑动，自动切换下一个状态
 * 2、触摸事件拦截分发，处理和其他滑动View的冲突问题
 * 三段式实现思路：
 * 1、增加一个中间状态，当View释放的时候，回弹到该状态。
 * @author RAE
 * @date 2022/08/24
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class MagicalBottomSheetBehavior<V : View>(context: Context, attrs: AttributeSet?) :
    BottomSheetBehavior<V>(context, attrs) {

    companion object {
        /** 中间状态 */
        const val STATE_MIDDLE = 0
    }

    /** 是否启用中间状态 */
    var isEnableMiddle = true

    /** 中间展开状态 */
    var middleExpandedRatio = 0.5F

    @SuppressLint("WrongConstant")
    override fun startSettlingAnimation(child: View, state: Int, top: Int, drag: Boolean) {
        if (!isEnableMiddle) return super.startSettlingAnimation(child, state, top, drag)

        // 计算peekHeight和展开的高度之间的距离
        val middleHeight = parentHeight * middleExpandedRatio
        val middleTop = (parentHeight - middleHeight) / 2
        val middleBottom = (parentHeight - middleHeight) + (middleHeight - peekHeight) / 2
        val viewTop = child.top.toFloat()
        if (state == STATE_MIDDLE || viewTop in middleTop..middleBottom) {
            MagicalUtils.debug("处于中间部分")
            super.startSettlingAnimation(child, state, middleHeight.toInt(), drag)
            setStateInternal(STATE_MIDDLE)
            return
        }
        MagicalUtils.debug("middleHeight【${state.formatState()}】： 父高度=[$parentHeight]；中间高度=$middleHeight；顶部=$middleTop；底部=$middleBottom;距离顶部=${child.top}")
        MagicalUtils.debug("onStateChanged【${state.formatState()}】： 父高度=[$parentHeight]；目标高度=$top；距离顶部=${child.top}")
        // 从底部
        super.startSettlingAnimation(child, state, top, drag)
    }


    private fun Int.formatState(): String {
        return when (this) {
            STATE_EXPANDED -> "展开"
            STATE_COLLAPSED -> "折叠"
            STATE_HALF_EXPANDED -> "HALF_EXPANDED"
            STATE_DRAGGING -> "DRAGGING"
            else -> this.toString()
        }
    }


}