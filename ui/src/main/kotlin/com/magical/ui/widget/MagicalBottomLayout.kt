//package com.magical.ui.widget
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.util.AttributeSet
//import android.view.View
//import android.view.ViewGroup
//import android.widget.FrameLayout
//import androidx.coordinatorlayout.widget.CoordinatorLayout
//import com.google.android.material.bottomsheet.BottomSheetBehavior
//import com.google.android.material.bottomsheet.BottomSheetDialog
//import com.google.android.material.bottomsheet.MagicalBottomBehavior
//import com.magical.ui.R
//
///**
// * 底部拖拽View，内部实现原理仿照[BottomSheetDialog]，由RAE原生开发。
// * @author RAE
// * @date 2022/08/07
// * @copyright Copyright (c) https://github.com/raedev All rights reserved.
// */
//@SuppressLint("ClickableViewAccessibility")
//@Suppress("MemberVisibilityCanBePrivate")
//class MagicalBottomLayout(context: Context, attrs: AttributeSet?) :
//    FrameLayout(context, attrs) {
//    constructor(context: Context) : this(context, null)
//
//
//    val behavior: MagicalBottomBehavior
//    val bottomSheet: FrameLayout
//    val coordinator: CoordinatorLayout
//
//    /** 内容布局 */
//    private var contentLayout: ViewGroup? = null
//
//    /** 默认展示的高度 */
//    private var peekHeight = -1
//
//    /** 默认展示的最小高度 */
//    private var minimizeHeight = -1
//
//    /** 默认展示的最大高度，一般是父布局的高度 */
//    private var peekMaxHeight = -1
//
//    init {
//        fitsSystemWindows = true
//        View.inflate(context, R.layout.magical_bottom_layout, this)
//        bottomSheet = findViewById(R.id.design_bottom_sheet)
//        coordinator = findViewById(R.id.coordinator)
//        behavior = BottomSheetBehavior.from(bottomSheet) as MagicalBottomBehavior
//        bottomSheet.setOnTouchListener { _, _ -> true }
//
//        context.obtainStyledAttributes(attrs, R.styleable.MagicalBottomLayout).also {
//            for (i in 0 until it.indexCount) {
//                when (val index = it.getIndex(i)) {
//                    R.styleable.MagicalBottomLayout_mg_peek_height -> {
//                        this.peekHeight = it.getDimension(index, -1.0f).toInt()
//                    }
//                    R.styleable.MagicalBottomLayout_mg_minimize_height -> {
//                        this.minimizeHeight = it.getDimension(index, -1.0f).toInt()
//                    }
//                    R.styleable.MagicalBottomLayout_mg_enable_minimize -> {
//                        this.behavior.enableMinimize = it.getBoolean(index, false)
//                    }
//                    R.styleable.MagicalBottomLayout_mg_hideable -> {
//                        this.behavior.isHideable = it.getBoolean(index, false)
//                    }
//                }
//            }
//            it.recycle()
//        }
//    }
//
//    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        super.onSizeChanged(w, h, oldw, oldh)
//        val parentView = parent as View
//        peekMaxHeight = parentView.measuredHeight
//        // 内容高度
//        updateContentHeight(peekMaxHeight)
//        // 如果没有设置高度，则为自动计算高度
//        if (peekHeight == -1) peekHeight = peekMaxHeight / 3
//        if (minimizeHeight == -1) minimizeHeight = peekHeight / 3
//        behavior.peekHeight = peekHeight
//        behavior.minimizeHeight = minimizeHeight
//    }
//
//    private fun updateContentHeight(height: Int) {
//        contentLayout?.layoutParams?.height = height
//    }
//
//    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
//        if (child?.id == R.id.coordinator) {
//            super.addView(child, params)
//            return
//        }
//        if (childCount > 2) throw IllegalStateException("MagicalBottomLayout只能存在一个子布局")
//        contentLayout = child as ViewGroup
//        bottomSheet.addView(child, params)
//    }
//
//    fun toggle() {
//        behavior.state = behavior.nextState()
//    }
//
//    fun hide() {
//        if (behavior.state == BottomSheetBehavior.STATE_HIDDEN) return
//        behavior.isHideable = true
//        behavior.state = BottomSheetBehavior.STATE_HIDDEN
//        behavior.isHideable = false
//    }
//
//    fun show() {
//        behavior.show()
//    }
//
//    fun getContentTop(): Int {
//        return contentLayout?.top ?: -1
//    }
//}