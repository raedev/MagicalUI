package com.magical.ui.dialog

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.magical.ui.R

/**
 * 弹出对话框
 * @author RAE
 * @date 2022/08/25
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class MagicalAlertDialogFragment : AppCompatDialogFragment() {


    var cancelCallback: ((MagicalAlertDialogFragment) -> Unit)? = null
    var confirmCallback: ((MagicalAlertDialogFragment) -> Unit)? = null

    override fun getTheme(): Int {
        return R.style.MagicalDialog_Alert
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.mg_alert_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments ?: return dismiss()
        val message = args.getString("message")
        val title = args.getString("title")
        val confirmText = args.getString("confirmText")
        val cancelText = args.getString("cancelText")
        val dialog = this.dialog as AppCompatDialog
        this.isCancelable = args.getBoolean("cancelable", true)
        val showCancelButton = args.getBoolean("showCancelButton", true)
        dialog.setCanceledOnTouchOutside(this.isCancelable)

        val messageView = view.findViewById<TextView>(android.R.id.message)
        messageView.text = message
        messageView.movementMethod = ScrollingMovementMethod()

        view.findViewById<TextView>(android.R.id.title).apply {
            title?.let { this.text = it }
        }
        view.findViewById<TextView>(android.R.id.message).apply {
            this.text = message
        }
        view.findViewById<Button>(R.id.btn_cancel).apply {
            this.visibility = if (showCancelButton) View.VISIBLE else View.GONE
            cancelText?.let { this.text = it }
            this.setOnClickListener {
                cancelCallback?.invoke(this@MagicalAlertDialogFragment) ?: dismiss()
            }
        }
        view.findViewById<Button>(R.id.btn_confirm).apply {
            confirmText?.let { this.text = it }
            this.setOnClickListener {
                confirmCallback?.invoke(this@MagicalAlertDialogFragment) ?: dismiss()
            }
        }
    }


}