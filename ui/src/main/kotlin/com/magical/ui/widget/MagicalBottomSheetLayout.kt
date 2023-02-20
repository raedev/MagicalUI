package com.magical.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.magical.ui.behavior.MagicalBottomSheetBehavior

/**
 * 底部三段式滑动布局
 * @author RAE
 * @date 2022/08/24
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class MagicalBottomSheetLayout : CoordinatorLayout {

    internal companion object {

        fun findCurrentAsParent(view: View): MagicalBottomSheetLayout? {
            var parent = view.parent
            while (parent != null) {
                if (parent is MagicalBottomSheetLayout) {
                    return parent
                }
                parent = parent.parent
            }
            return null
        }
    }

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    private lateinit var _behavior: MagicalBottomSheetBehavior<View>

    var behavior: MagicalBottomSheetBehavior<View>
        get() = _behavior
        set(value) {
            _behavior = value
            if (childCount > 0) {
                (getChildAt(0).layoutParams as LayoutParams).behavior = value
                invalidate()
            }
        }

    private fun initView() {
        _behavior = MagicalBottomSheetBehavior(context, null)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        val p = params as LayoutParams
        if (child is ViewGroup && p.behavior == null) {
            p.behavior = this.behavior
        }
    }


}



