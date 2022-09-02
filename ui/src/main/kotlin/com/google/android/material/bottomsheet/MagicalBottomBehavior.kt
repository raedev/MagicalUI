//package com.google.android.material.bottomsheet
//
//import android.content.Context
//import android.util.AttributeSet
//import android.view.MotionEvent
//import android.view.View
//import android.view.ViewGroup
//import android.widget.FrameLayout
//import android.widget.ScrollView
//import androidx.coordinatorlayout.widget.CoordinatorLayout
//import androidx.core.view.children
//import androidx.customview.widget.ViewDragHelper
//import androidx.recyclerview.widget.RecyclerView
//import com.magical.ui.widget.BottomLinearLayoutManager
//
///**
// * 底部滑动行为算法，由Rae开发。
// * @author RAE
// * @date 2022/08/07
// * @copyright Copyright (c) https://github.com/raedev All rights reserved.
// */
//class MagicalBottomBehavior(context: Context, attrs: AttributeSet) :
//    BottomSheetBehavior<FrameLayout>(context, attrs) {
//
//    companion object {
//        const val TAG = "rae"
//
//        /**
//         * 最小化状态
//         */
//        const val STATE_MINIMIZE = 7
//
//        /** 方向上 */
//        const val DIRECTION_UP = 0
//
//        /** 方向下 */
//        const val DIRECTION_DOWN = 1
//    }
//
//    /** 最小化时的高度 */
//    var minimizeHeight: Int = -1
//
//    /** 是否有滚动的子View */
//    private var scrollingChildView: View? = null
//
//    /**
//     * 是否允许最小化
//     */
//    var enableMinimize: Boolean = false
//        set(value) {
//            field = value
//            if (this::dragCallback.isInitialized) {
//                dragCallback.enableMinimize = value
//            }
//        }
//
//    private lateinit var dragCallback: MagicalBottomDragCallback
//
//    override fun onLayoutChild(
//        parent: CoordinatorLayout,
//        child: FrameLayout,
//        layoutDirection: Int
//    ): Boolean {
//        // 在布局的时候找View，节省开销
//        scrollingChildView = findScrollChildView(child)
//        (scrollingChildView as RecyclerView?)?.let {
//            if (it.layoutManager != null && it.layoutManager !is BottomLinearLayoutManager) {
//                throw IllegalStateException("RecyclerView 中的 layoutManager必须为BottomLinearLayoutManager")
//            }
//        }
//        val field = this.javaClass.superclass.getDeclaredField("dragCallback")
//        field.isAccessible = true
//        val sourceHelper = field.get(this) as ViewDragHelper.Callback
//        dragCallback = MagicalBottomDragCallback(sourceHelper, this)
//        dragCallback.enableMinimize = this.enableMinimize
//        viewDragHelper = ViewDragHelper.create(parent, dragCallback)
//        return super.onLayoutChild(parent, child, layoutDirection)
//    }
//
//
//    /** Touch事件拦截 */
//    override fun onInterceptTouchEvent(
//        parent: CoordinatorLayout,
//        child: FrameLayout,
//        event: MotionEvent
//    ): Boolean {
//        // 只要内容布局的滚动View不在顶部，不拦截事件，都交给内容布局去处理。
//        if (scrollingChildView != null
//            && event.action != MotionEvent.ACTION_DOWN
//            && scrollingChildView!!.canScrollVertically(-1)
//        ) return false
//        return super.onInterceptTouchEvent(parent, child, event)
//    }
//
//
//    override fun setStateInternal(state: Int) {
//        if (this.state == state) {
//            return
//        }
//        super.setStateInternal(state)
//        if (state == STATE_COLLAPSED ||
//            state == STATE_EXPANDED ||
//            state == STATE_MINIMIZE ||
//            state == STATE_HALF_EXPANDED ||
//            hideable && state == STATE_HIDDEN
//        ) {
//            this.lastStableState = state
//        }
//    }
//
//    public override fun settleToState(child: View, state: Int) {
//        if (enableMinimize && state == STATE_MINIMIZE) {
//            startSettlingAnimation(child, state, parentHeight - minimizeHeight, false)
//            return;
//        }
//        super.settleToState(child, state)
//    }
//
//    override fun startSettlingAnimation(
//        child: View?,
//        state: Int,
//        top: Int,
//        settleFromViewDragHelper: Boolean
//    ) {
//        val touchTimeSpan = dragCallback.touchTimeSpan
////        Log.d(
////            TAG,
////            "startSettlingAnimation: state=$state, last=$lastStableState, time=$touchTimeSpan"
////        )
//        var newTop = top
//        var newState = state
//        // 计算从触摸开始到释放的时间，如果>400ms说明是慢慢移动的
//        if (enableMinimize && !hideable && touchTimeSpan < 200 && lastStableState == STATE_MINIMIZE) {
//            // 如果是最小化的状态，下一个状态是正常状态
//            newState = STATE_COLLAPSED
//            newTop = collapsedOffset
//        }
//        super.startSettlingAnimation(child, newState, newTop, settleFromViewDragHelper)
//    }
//
//    /**
//     * 找是否有可以滚动的View
//     */
//    private fun findScrollChildView(view: View): View? {
//        if (view is ViewGroup) {
//            view.children.forEach { itView ->
//                if (itView is RecyclerView || itView is ScrollView) return itView
//                if (itView is ViewGroup && itView.childCount > 0) {
//                    val childrenView = findScrollChildView(itView)
//                    if (childrenView != null) return childrenView
//                }
//            }
//        }
//        return null
//    }
//
//    /**
//     * 获取下一个状态
//     */
//    fun nextState(): Int {
//        // 如果是最小的状态，切换方向为向上
//        var toggleDirection = DIRECTION_UP
//        if (enableMinimize && state == STATE_MINIMIZE) {
//            toggleDirection = DIRECTION_UP
//        }
//        // 如果是最小的状态，切换方向为向上
//        if (isHideable && state == STATE_HIDDEN) {
//            toggleDirection = DIRECTION_UP
//        }
//        // 如果是展开的状态，切换方向为向下
//        if (state == STATE_EXPANDED) {
//            toggleDirection = DIRECTION_DOWN
//        }
//        return when (state) {
//            // 正常状态
//            STATE_COLLAPSED -> {
//                if (toggleDirection == DIRECTION_UP) STATE_EXPANDED
//                else if (enableMinimize) STATE_MINIMIZE
//                else if (isHideable) STATE_HIDDEN
//                else STATE_EXPANDED
//            }
//            else -> {
//                if (enableMinimize && dragCallback.touchReleaseYVel > 0) STATE_MINIMIZE
//                else STATE_COLLAPSED
//            }
//        }
//    }
//
//
//    fun show() {
//        var state = lastStableState
//        if (lastStableState == STATE_HIDDEN) {
//            state = STATE_COLLAPSED
//        }
//        setState(state)
//    }
//}