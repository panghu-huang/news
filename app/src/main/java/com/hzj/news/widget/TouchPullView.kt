package com.hzj.news.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.BounceInterpolator

/**
 * Created by Logan on 2017/6/8.
 */

class TouchPullView : View {
    // 圆形画笔
    private var mCirclePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    // 圆形半径
    private val mRadius = 40f
    // 贝塞尔曲线画笔
    private var mPathPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    // 最大下拉距离
    val DRAG_HEIGHT_MAX = 200
    // 控制点最大纵轴距离
    private val CONTROL_HEIGHT_MAX = 10
    // 二阶贝塞尔曲线起点-X坐标
    private var mStartPointX = 0f
    // 二阶贝塞尔曲线控制点
    private var mControlPoint: PointF = PointF(0f, 0f)
    // 二阶贝塞尔曲线终点
    private var mEndPoint: PointF = PointF()
    // 二阶贝塞尔曲线路径
    private var mPath: Path = Path()
    // 下拉进度
    private var mProgress: Float = 0f
    // View高度
    private var mHeight: Float = DRAG_HEIGHT_MAX.toFloat()
    // View宽度
    private var mWidth: Float = 0f

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        mCirclePaint.color = Color.GRAY
        mCirclePaint.strokeWidth = 5f
        mCirclePaint.style = Paint.Style.FILL
        mCirclePaint.isAntiAlias = true
        mCirclePaint.isDither = true

        mPathPaint.color = Color.parseColor("#3F51B5")
        mPathPaint.style = Paint.Style.FILL
        mPathPaint.isDither = true
        mPathPaint.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        if (widthMode == MeasureSpec.AT_MOST ||
                widthMode == MeasureSpec.EXACTLY) {
            mWidth = Math.max(mWidth, width.toFloat())
        }
        mHeight = DRAG_HEIGHT_MAX * mProgress
        setMeasuredDimension(mWidth.toInt(), mHeight.toInt())

        setBezierPoints()
    }

    private fun setBezierPoints() {
        mStartPointX = width / 4 * mProgress
        mControlPoint.x = width.toFloat() / 4 * (1 + mProgress)
        mControlPoint.y = CONTROL_HEIGHT_MAX * mProgress
        val length = Math.sqrt(((mWidth / 2 - mControlPoint.x) * (mWidth / 2 - mControlPoint.x) +
                (mHeight - mRadius) * (mHeight - mRadius)).toDouble())
        val cos = (mHeight - mRadius) / length
        val sin = (mWidth / 2 - mControlPoint.x) / length
        mEndPoint.x = (mWidth / 2 - mRadius * cos).toFloat()
        mEndPoint.y = ((mHeight - mRadius) + mRadius * sin).toFloat()
        val path = mPath
        path.reset()
        path.moveTo(mStartPointX, 0f)
        path.quadTo(mControlPoint.x, mControlPoint.y,
                mEndPoint.x, mEndPoint.y)
        path.lineTo(mWidth - mEndPoint.x, mEndPoint.y)
        path.quadTo(mWidth - mControlPoint.x, mControlPoint.y,
                mWidth - mStartPointX, 0f)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(mWidth.toInt(), mHeight.toInt(), oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(mWidth / 2, mHeight - mRadius, mRadius, mPathPaint)
        canvas.drawPath(mPath, mPathPaint)
        canvas.drawPoint(mStartPointX, 0f, mPathPaint)
        canvas.drawPoint(mControlPoint.x, mControlPoint.y, mPathPaint)
        canvas.drawCircle(mWidth / 2, mHeight - mRadius, mRadius - 10, mCirclePaint)
    }

    fun setProgress(progress: Float): Unit {
        mProgress = progress
        requestLayout()
    }

    fun startAnim() {
        val animator = ValueAnimator.ofFloat(mProgress, 0f)
        animator.duration = 500
        animator.interpolator = BounceInterpolator()
        animator.addUpdateListener {
            valueAnimator ->
            val value: Float = valueAnimator.animatedValue as Float
            mProgress = value
            requestLayout()
        }
        animator.start()
    }
}
