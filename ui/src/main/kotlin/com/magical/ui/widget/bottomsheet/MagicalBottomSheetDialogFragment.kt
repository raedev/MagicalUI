package com.magical.ui.widget.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.magical.ui.R
import com.magical.ui.dialog.BottomSheetAlertDialog

/**
 * 底部弹出对话框
 * @author RAE
 * @date 2022/08/25
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class MagicalBottomSheetDialogFragment : BottomSheetDialogFragment() {

    var cancelCallback: ((BottomSheetDialogFragment) -> Unit)? = null
    var confirmCallback: ((BottomSheetDialogFragment) -> Unit)? = null

    override fun getTheme(): Int {
        return R.style.MagicalAlertBottomSheetDialog
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetAlertDialog(requireContext(), theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.mg_bottom_sheet_alert_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments ?: return dismiss()
        val message = args.getString("message")
        val title = args.getString("title")
        val confirmText = args.getString("confirmText")
        val cancelText = args.getString("cancelText")
        val dialog = this.dialog as BottomSheetAlertDialog
        this.isCancelable = args.getBoolean("cancelable", true)
        val showCancelButton = args.getBoolean("showCancelButton", true)
        dialog.setCanceledOnTouchOutside(this.isCancelable)
        dialog.behavior.isDraggable = false

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
                cancelCallback?.invoke(this@MagicalBottomSheetDialogFragment) ?: dismiss()
            }
        }
        view.findViewById<Button>(R.id.btn_confirm).apply {
            confirmText?.let { this.text = it }
            this.setOnClickListener {
                confirmCallback?.invoke(this@MagicalBottomSheetDialogFragment) ?: dismiss()
            }
        }
    }

}