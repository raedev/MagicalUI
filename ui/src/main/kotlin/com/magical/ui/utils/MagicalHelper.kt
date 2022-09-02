package com.magical.ui.utils

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.annotation.StyleableRes

/**
 *
 * @author RAE
 * @date 2022/08/24
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
internal open class MagicalHelper(
    context: Context,
    attrSet: AttributeSet?,
    @StyleableRes attrs: IntArray,
    @AttrRes defStyleAttr: Int,
    @StyleRes defStyleRes: Int
) {

    constructor(
        context: Context,
        attrSet: AttributeSet?,
        @StyleableRes attrs: IntArray
    ) : this(context, attrSet, attrs, 0, 0)

    companion object {
        const val UNDEFINED = -1
    }

    init {
        context.obtainStyledAttributes(attrSet, attrs, defStyleAttr, defStyleRes).also {
            for (i in 0 until it.indexCount) {
                val index = it.getIndex(i)
                onParseAttributes(it, index)
            }
            it.recycle()
        }
    }

    protected open fun onParseAttributes(attr: TypedArray, index: Int) {

    }

    protected fun debug(msg: String) = MagicalUtils.debug(msg)
}