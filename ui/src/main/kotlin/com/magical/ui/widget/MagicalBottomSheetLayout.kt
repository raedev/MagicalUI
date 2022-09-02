package com.magical.ui.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.MagicalBottomSheetBehavior
import com.magical.ui.R
import com.magical.ui.utils.MagicalUtils

/**
 * 底部滑动布局
 * @author RAE
 * @date 2022/08/24
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class MagicalBottomSheetLayout : CoordinatorLayout {

    companion object {
        /**
         * 包装一层
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

    /** 遮罩层 */
    private val maskView: View
    private val contentLayout: LinearLayout
    private val behavior: MagicalBottomSheetBehavior<LinearLayout>

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.fitsSystemWindows = true
        this.id = R.id.coordinator
        this.behavior = MagicalBottomSheetBehavior(context, attrs)
        this.behavior.middleExpandedRatio = 0.5F
        this.behavior.peekHeight = 600

        // 遮罩层
        maskView = View(context).apply {
            this.id = R.id.mask
            this.isSoundEffectsEnabled = false
            this.setBackgroundColor(Color.parseColor("#4FFF0000"))
            ViewCompat.setImportantForAccessibility(this, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO)
            this.setOnClickListener { behavior.state = BottomSheetBehavior.STATE_COLLAPSED }
        }

        // 内容布局
        contentLayout = LinearLayout(context).apply {
            this.id = R.id.bottom_sheet
            this.orientation = LinearLayout.VERTICAL
            val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            params.behavior = this@MagicalBottomSheetLayout.behavior
            this.layoutParams = params
        }
        super.addView(maskView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        super.addView(contentLayout)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child == maskView || child == contentLayout) {
            return super.addView(child, index, params)
        }
        MagicalUtils.debug("添加View：$child")
        // 子View添加到内容布局中去
        contentLayout.addView(child, index, params)
    }

}