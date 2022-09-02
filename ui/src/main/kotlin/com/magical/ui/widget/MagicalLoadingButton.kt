package com.magical.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet

/**
 * 有加载的功能的按钮
 * @author RAE
 * @date 2022/08/24
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class MagicalLoadingButton : MagicalButton {

    private var loadingIcon: Drawable
    private val normalText: CharSequence

    /** 加载中状态 */
    var isLoading: Boolean = false
        set(value) {
            field = value
            this.isEnabled = !value
            if (value) {
                helper.loadingText?.let { this.text = it }
                this.icon = loadingIcon
                this.icon.setVisible(value, true)
            } else {
                this.text = normalText
                this.icon = null
            }
            invalidate()
        }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.normalText = text
        this.loadingIcon = icon
        this.isLoading = false
    }

}