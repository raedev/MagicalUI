package com.magical.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.magical.ui.R

/**
 * 加载中对话框
 * @author RAE
 * @date 2022/08/25
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class MagicalLoadingDialogFragment : AppCompatDialogFragment() {

    private var messageView: TextView? = null

    var message: String? = null
        set(value) {
            field = value ?: return
            arguments?.putString("message", value)
            messageView?.text = value
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.mg_loading_alert_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val message = arguments?.getString("message")
        messageView = view.findViewById(android.R.id.message)
        message?.let { messageView!!.text = it }
        val dialog = dialog as AppCompatDialog
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setDimAmount(0.3F)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


}