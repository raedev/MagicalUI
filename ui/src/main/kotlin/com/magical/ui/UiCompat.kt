package com.magical.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.magical.ui.dialog.MagicalAlertDialogFragment
import com.magical.ui.dialog.MagicalLoadingDialogFragment
import com.magical.ui.widget.bottomsheet.MagicalBottomSheetDialogFragment
import java.lang.ref.WeakReference

/**
 * 常用的UI操作：Toast、Dialog
 * @author RAE
 * @date 2022/08/22
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
@Suppress("MemberVisibilityCanBePrivate")
object UiCompat {

    // region Toast操作

    fun showToast(fragment: Fragment, message: String) =
        showToast(fragment.requireContext(), message)

    /**
     * 显示提示信息。
     * 从设计角度，不允许从线程中调用这个方法，请规范业务代码回调到主线程中调用。
     */
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // endregion

    // region 动画操作

    /**
     * 开始执行动画
     */
    fun startAnimation(view: View?, @AnimRes resId: Int, duration: Long = 0) {
        view ?: return
        val animation = AnimationUtils.loadAnimation(view.context, resId)
        if (duration > 0) {
            animation.duration = duration
        }
        view.startAnimation(animation)
    }

    /**
     * 淡入淡出动画
     */
    fun startFadeAnimation(view: View?, visibility: Boolean, duration: Long = 0) {
        startAnimation(
            view,
            if (visibility) android.R.anim.fade_in else android.R.anim.fade_out,
            duration
        )
    }

    /**
     * 设置可见和隐藏
     */
    fun setVisibility(view: View, visibility: Boolean) {
        view.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    /**
     * 设置可见和不可见
     */
    fun setInVisibility(view: View, visibility: Boolean) {
        view.visibility = if (visibility) View.VISIBLE else View.INVISIBLE
    }


    // endregion

    // region Dialog

    /** 全局Loading框引用 */
    private var loadingDialogRef: WeakReference<MagicalLoadingDialogFragment>? = null

    /**
     * 显示加载中对话框，有且只有一个
     */
    fun showLoading(owner: LifecycleOwner, message: String? = null): MagicalLoadingDialogFragment? {
        val manager = when (owner) {
            is FragmentActivity -> owner.supportFragmentManager
            is Fragment -> owner.childFragmentManager
            else -> return null
        }
        // 检查当前的对话框是否是自己的
        loadingDialogRef?.get()?.let {
            if (it.isDetached || it.fragmentManager == null) {
                loadingDialogRef!!.clear()
                return@let
            }
            if (it.fragmentManager != manager) {
                // 关闭当前的
                it.dismiss()
                loadingDialogRef!!.clear()
            }
        }
        val tag = "LoadingDialog"
        val createDialogFun: () -> MagicalLoadingDialogFragment = {
            MagicalLoadingDialogFragment().apply {
                this.arguments = Bundle().apply { this.putString("message", message) }
                loadingDialogRef = WeakReference(this)
            }
        }
        val fm = loadingDialogRef?.get() ?: createDialogFun()
        fm.message = message
        if (!fm.isVisible) fm.show(manager, tag)
        return fm
    }

    fun dismiss() {
        loadingDialogRef?.get()?.dismiss()
        loadingDialogRef?.clear()
        loadingDialogRef = null
    }

    fun showAlertDialog(
        owner: LifecycleOwner,
        message: String,
        title: String? = null,
        cancelable: Boolean = true,
        showCancelButton: Boolean = false,
        confirmText: String? = null,
        cancelText: String? = null,
        confirmCallback: ((AppCompatDialogFragment) -> Unit)? = null,
        cancelCallback: ((AppCompatDialogFragment) -> Unit)? = null,
    ): AppCompatDialogFragment {
        // 兼容加载中
        dismiss()
        val manager = when (owner) {
            is FragmentActivity -> owner.supportFragmentManager
            is Fragment -> owner.childFragmentManager
            else -> throw IllegalStateException("不支持的类型：$owner")
        }
        val fragment = MagicalAlertDialogFragment()
        fragment.arguments = Bundle().apply {
            this.putBoolean("cancelable", cancelable)
            this.putBoolean("showCancelButton", showCancelButton)
            this.putString("message", message)
            this.putString("title", title)
            this.putString("confirmText", confirmText)
            this.putString("cancelText", cancelText)
        }
        fragment.confirmCallback = confirmCallback
        fragment.cancelCallback = cancelCallback
        fragment.show(manager, "BottomSheetAlertDialog")
        return fragment
    }

    /**
     * 显示底部弹窗
     */
    fun showBottomDialog(
        owner: LifecycleOwner,
        message: String,
        title: String? = null,
        cancelable: Boolean = true,
        showCancelButton: Boolean = false,
        confirmText: String? = null,
        cancelText: String? = null,
        confirmCallback: ((BottomSheetDialogFragment) -> Unit)? = null,
        cancelCallback: ((BottomSheetDialogFragment) -> Unit)? = null,
    ): BottomSheetDialogFragment {
        dismiss()
        val manager = when (owner) {
            is FragmentActivity -> owner.supportFragmentManager
            is Fragment -> owner.childFragmentManager
            else -> throw IllegalStateException("不支持的类型：$owner")
        }
        val fragment = MagicalBottomSheetDialogFragment()
        fragment.arguments = Bundle().apply {
            this.putBoolean("cancelable", cancelable)
            this.putBoolean("showCancelButton", showCancelButton)
            this.putString("message", message)
            this.putString("title", title)
            this.putString("confirmText", confirmText)
            this.putString("cancelText", cancelText)
        }
        fragment.confirmCallback = confirmCallback
        fragment.cancelCallback = cancelCallback
        fragment.show(manager, "BottomSheetAlertDialog")
        return fragment
    }


    // endregion

}

