//package com.google.android.material.bottomsheet
//
//import android.annotation.SuppressLint
//import android.view.View
//import androidx.core.math.MathUtils
//import androidx.customview.widget.ViewDragHelper
//
///**
// * 代理原理的Callback，并实现最小化功能
// * @author RAE
// * @date 2022/08/08
// * @copyright Copyright (c) https://github.com/raedev All rights reserved.
// */
//class MagicalBottomDragCallback(
//    private val source: ViewDragHelper.Callback,
//    private val behavior: MagicalBottomBehavior
//) :
//    ViewDragHelper.Callback() {
//
//    var enableMinimize: Boolean = true
//
//    // 计算从触摸开始都释放的间隔时间ms
//    internal var touchTimeSpan = 0L
//
//    /** 触摸开始的时间 */
//    private var touchStartTime = 0L
//
//    // 触摸的Y轴
//    internal var touchReleaseYVel = 0F
//
//
//    override fun tryCaptureView(child: View, pointerId: Int): Boolean {
//        touchStartTime = System.currentTimeMillis()
//        return source.tryCaptureView(child, pointerId)
//    }
//
//    override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
//        source.onViewPositionChanged(changedView, left, top, dx, dy)
//    }
//
//    override fun onViewDragStateChanged(state: Int) {
//        source.onViewDragStateChanged(state)
//    }
//
//    @SuppressLint("WrongConstant")
//    override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
//        touchReleaseYVel = yvel
//        if (!enableMinimize) {
//            // 不启用最小化
//            source.onViewReleased(releasedChild, xvel, yvel)
//            return
//        }
//        touchTimeSpan = System.currentTimeMillis() - touchStartTime
//        if (releasedChild.top > (behavior.parentHeight - behavior.peekHeight / 2)) {
//            behavior.settleToState(releasedChild, MagicalBottomBehavior.STATE_MINIMIZE)
//            return
//        }
//        // 快速上下滑动
////        if (touchTimeSpan < 200) {
////            return if (yvel > 0) {
////                // 下滑动
////                behavior.settleToState(
////                    releasedChild,
////                    if (enableMinimize) MagicalBottomBehavior.STATE_MINIMIZE else BottomSheetBehavior.STATE_COLLAPSED
////                )
////            } else {
////                // 上滑动
////                behavior.settleToState(releasedChild, BottomSheetBehavior.STATE_EXPANDED)
////            }
////        }
//        source.onViewReleased(releasedChild, xvel, yvel)
//    }
//
//    override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
//        // 不启用最小化
//        if (!enableMinimize) return source.clampViewPositionVertical(child, top, dy)
//        return MathUtils.clamp(
//            top, behavior.getExpandedOffset(), behavior.parentHeight - behavior.minimizeHeight
//        )
//    }
//
//    override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
//        return 0
//    }
//
//    override fun getViewVerticalDragRange(child: View): Int {
//        return source.getViewVerticalDragRange(child)
//    }
//
//}