package com.magical.ui.widget.bottomsheet

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.MagicalBottomSheetBehavior

/**
 * 底部滑动布局，支持四段式滑动（展开-中部-可见-隐藏）
 * @author RAE
 * @date 2022/08/24
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class MagicalBottomSheetLayout : CoordinatorLayout {

    companion object {
        /**
         * 把View包装在滑动布局中去
         */
        fun wrap(view: View): MagicalBottomSheetLayout {
            val layout = MagicalBottomSheetLayout(view.context)
            layout.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            layout.addView(
                view,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            return layout
        }
    }

    private val contentLayout: LinearLayout
    var behavior: MagicalBottomSheetBehavior
        set(value) {
            @Suppress("SENSELESS_COMPARISON")
            if (field != null) {
                contentLayout.layoutParams = (contentLayout.layoutParams as LayoutParams).apply {
                    val current = this.behavior as MagicalBottomSheetBehavior
                    value.peekHeight = current.peekHeight
                    this.behavior = value
                }
            }
            field = value
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.behavior = MagicalBottomSheetBehavior(context, attrs)
        this.behavior.peekHeight = -1

        // 内容布局
        contentLayout = LinearLayout(context).apply {
            this.orientation = LinearLayout.VERTICAL
            val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            params.behavior = this@MagicalBottomSheetLayout.behavior
            this.layoutParams = params
        }
        superAddView(contentLayout)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (this.behavior.peekHeight == -1) {
            // 若没有设置可见高度，默认为当前布局的1/3高度。
            this.behavior.peekHeight = (measuredHeight * 0.3F).toInt()
        }
    }

    private fun superAddView(child: View) {
        super.addView(child, -1, child.layoutParams)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        // 子View添加到内容布局中去
        contentLayout.addView(child, index, params)
    }

}