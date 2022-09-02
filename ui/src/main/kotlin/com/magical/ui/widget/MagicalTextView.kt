package com.magical.ui.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import com.google.android.material.textview.MaterialTextView

/**
 * 文本
 * @author RAE
 * @date 2022/08/07
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class MagicalTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    MaterialTextView(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        android.R.attr.textViewStyle
    )

    /** 选中的时候变粗体 */
    var enableSelectedBold: Boolean = false

    @DrawableRes
    var drawableTop: Int = -1
        set(value) {
            field = value
            setCompoundDrawablesRelativeWithIntrinsicBounds(0, value, 0, 0)
        }

    var textStyle: Int = -1
        set(value) {
            field = value
            if (this.typeface == null) this.typeface = Typeface.defaultFromStyle(value)
            else this.typeface = Typeface.create(this.typeface, value)
        }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        if (enableSelectedBold) {
            textStyle = if (selected) Typeface.BOLD else Typeface.NORMAL
        }
    }
}