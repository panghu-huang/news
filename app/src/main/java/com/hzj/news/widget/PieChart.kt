package com.hzj.news.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by Logan on 2017/6/11.
 */
class PieChart : View {
    // 画笔
    private var mPaint: Paint? = null
    // 要画的项
    private var items: ArrayList<HashMap<String, Any>>? = null
    // 项中Num值得总数
    private var count = 0
    // 弧形外接圆
    private var rect: RectF? = null
    // 圆形圆心
    private var mCenterPoint: PointF? = null
    // 圆形半径
    private var mRadius = 0f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint?.style = Paint.Style.FILL
        mPaint?.isAntiAlias = true
        mPaint?.isDither = true

        items = ArrayList<HashMap<String, Any>>()
        rect = RectF()

        mCenterPoint = PointF(0f, 0f)
    }

    fun addItem(item: HashMap<String, Any>) {
        items?.add(item)
        count += item["num"] as Int
    }

    fun removeAllItem() {
        if (items!!.size != 0) {
            items!!.clear()
            count = 0
        }
    }

    fun drawPieChart() {
        if (items!!.size != 0) {
            var startAngle = 0f
            for (item in items!!) {
                val percent = ((item["num"] as Int).toFloat() / count)
                val endAngle = percent * 360
                item.put("endAngle", endAngle)
                item.put("startAngle", startAngle)
                item.put("sweepAngle", 0f)
                item.put("percent", percent)
                startAngle += endAngle
            }
            changeSweepAngle(0)
        }
    }

    private fun changeSweepAngle(position: Int) {
        val item: HashMap<String, Any> = items!![position]
        val endAngle = item["endAngle"] as Float
        val animator = ValueAnimator.ofFloat(0f, endAngle)
        animator.duration = 500
        animator.start()
        animator.addUpdateListener {
            valueAnimator ->
            val sweepAngle: Float = valueAnimator.animatedValue as Float
            item.put("sweepAngle", sweepAngle)
            invalidate()
            if (sweepAngle >= endAngle) {
                if (position != items!!.size - 1) {
                    changeSweepAngle(position + 1)
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = measuredHeight
        val rectLength = Math.min(width, height) - 40
        rect?.left = ((width - rectLength) / 2).toFloat()
        rect?.right = width - rect!!.left
        rect?.top = ((height - rectLength) / 2).toFloat()
        rect?.bottom = height - rect!!.top

        mCenterPoint!!.x = width / 2.toFloat()
        mCenterPoint!!.y = height / 2.toFloat()
        mRadius = rectLength / 2.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        val paint = mPaint
        paint!!.color = Color.DKGRAY
        canvas.drawCircle(mCenterPoint!!.x, mCenterPoint!!.y, mRadius, paint)
        for (item in items!!) {
            paint.color = item["color"] as Int
            canvas.drawArc(rect, item["startAngle"] as Float,
                    item["sweepAngle"] as Float, true, paint)
        }
    }

    fun getItems(): ArrayList<HashMap<String, Any>> {
        return items!!
    }
}