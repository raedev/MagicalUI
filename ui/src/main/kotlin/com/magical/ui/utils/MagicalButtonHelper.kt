package com.magical.ui.utils

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.StyleableRes
import com.magical.ui.R

/**
 * 按钮帮助
 * @author RAE
 * @date 2022/08/24
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
internal class MagicalButtonHelper(
    context: Context,
    attrSet: AttributeSet?,
    @StyleableRes attrs: IntArray
) : MagicalHelper(context, attrSet, attrs) {

    /** 不可用颜色 */
    var disableColor: Int = UNDEFINED

    /** 按下颜色 */
    var pressedColor: Int = UNDEFINED

    /** 加载中文本 */
    var loadingText: String? = null

    override fun onParseAttributes(attr: TypedArray, index: Int) {
        super.onParseAttributes(attr, index)
        when (index) {
            R.styleable.MagicalButton_mg_color_pressed ->
                this.pressedColor = attr.getColor(index, -1)
            R.styleable.MagicalButton_mg_color_disable ->
                this.disableColor = attr.getColor(index, -1)
            R.styleable.MagicalButton_mg_loading_text ->
                this.loadingText = attr.getString(index)
        }
    }


}