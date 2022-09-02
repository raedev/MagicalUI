package com.magical.ui.dialog

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.magical.ui.R

/**
 * 底部弹出对话框
 * @author RAE
 * @date 2022/08/25
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class BottomSheetAlertDialog(context: Context, themeId: Int) :
    BottomSheetDialog(context, themeId) {

    override fun setContentView(view: View) {
        super.setContentView(view)
        view.setBackgroundResource(R.drawable.mg_bg_bottom_sheet_card_top)
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        view.setBackgroundResource(R.drawable.mg_bg_bottom_sheet_card_top)
    }

}