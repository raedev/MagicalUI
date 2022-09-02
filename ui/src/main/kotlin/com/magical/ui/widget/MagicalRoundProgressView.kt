package com.magical.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.magical.ui.R
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * 圆环进度
 * @author RAE
 * @date 2022/08/24
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class MagicalRoundProgressView : AppCompatTextView {

    //    private var animator: ValueAnimator = ValueAnimator.ofInt(0)
    private val mRect = RectF()
    private var mBackgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mProgressPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var lastProgress = 0
    private var finishedText = "已完成"
    private var normalText = "待开始"

    var maxProgress = 100
        get
        set(value) {
            if (value <= 0) return
            field = value
            invalidate()
        }

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    private fun initView() {
        mBackgroundPaint.color = ContextCompat.getColor(context, R.color.dividerPrimary)
        mBackgroundPaint.strokeWidth = dp2px(8).toFloat()
        mBackgroundPaint.style = Paint.Style.STROKE
        mProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mProgressPaint.color = ContextCompat.getColor(context, R.color.colorPrimary)
        mProgressPaint.strokeWidth = dp2px(8).toFloat()
        mProgressPaint.style = Paint.Style.STROKE
        this.gravity = Gravity.CENTER
        this.progress = -1
    }

    var progress: Int = -1
        get
        set(value) {
            // 保存上一次进度
            lastProgress = value
            // 更新进度
            var progressValue: Int = (value * 0.1f / (maxProgress * 0.1f) * 100.0f).roundToInt()
            progressValue = max(0, min(progressValue, 100))
            this.text = when {
                value == -1 -> normalText
                progressValue >= 100 -> this.finishedText
                else -> "$progressValue%"
            }
            field = progressValue
            invalidate()
        }


    private fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    override fun onDraw(canvas: Canvas) {
        val padding = dp2px(4)
        mRect.set(
            padding.toFloat(),
            padding.toFloat(),
            (width - padding).toFloat(),
            (height - padding).toFloat()
        )

        // 转为为圆的角度
        val angle: Int = (360.0f * progress / 100).toInt()

        // 画背景圆
        canvas.drawArc(
            mRect, -90f, 360f,
            false,
            mBackgroundPaint
        )

        // 画进度圆
        canvas.drawArc(
            mRect, -90f,
            angle.toFloat(),
            false,
            mProgressPaint
        )
        super.onDraw(canvas)
    }
}