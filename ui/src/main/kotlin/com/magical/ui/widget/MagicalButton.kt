package com.magical.ui.widget

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton
import com.magical.ui.R
import com.magical.ui.utils.MagicalButtonHelper

/**
 * 按钮
 * @author RAE
 * @date 2022/08/07
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class MagicalButton : MaterialButton {

    internal val helper: MagicalButtonHelper

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        helper = MagicalButtonHelper(context, attrs, R.styleable.MagicalButton)
    }

}