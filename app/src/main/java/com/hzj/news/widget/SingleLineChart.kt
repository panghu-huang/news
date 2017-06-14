package com.hzj.news.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * Created by Logan on 2017/6/11.
 */
class SingleLineChart : LinearLayout {
    private var mPaint: Paint? = null
    private var mData: HashMap<String, Any>? = null
    private var mDataColor = Color.BLUE
    private var mPercentColor = Color.GREEN
    private var mPercent = .6f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint?.style = Paint.Style.FILL
        mPaint?.isAntiAlias = true
        mPaint?.isDither = true
        setWillNotDraw(false)
    }

    fun setData(data: HashMap<String, Any>) {
        mPercent = data["percent"] as Float
        mDataColor = data["color"] as Int
        mPercentColor = data["percentColor"] as Int
        mData = data
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        var colorWidth = 20f
        if (paddingLeft > 30) {
            colorWidth = (paddingLeft - 20).toFloat()
        }

        val width = measuredWidth - colorWidth
        val height = measuredHeight.toFloat()
        mPaint!!.color = mDataColor
        canvas.drawRect(0f, 0f, colorWidth, height, mPaint)
        mPaint!!.color = mPercentColor
        canvas.drawRect(colorWidth, 0f, colorWidth + (width * mPercent), height, mPaint)
    }
}