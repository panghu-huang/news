package com.hzj.news.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import com.hzj.news.Fragment.AnalysisFragment

/**
 * Created by Logan on 2017/6/10.
 */
class BottomBar : LinearLayout {

    // View宽度
    private var mWidth = 0
    // View高度
    private var mHeight = 0
    // 子View数量
    private var mChildCount = 0
    // 当前选中的索引
    private var mCurrentIndex = 0
    // 点中的View中的坐标 X
    private var mCurrentX = -1f
    // 点中的View中的坐标 X
    private var mCurrentY = -1f
    // 上一次选中的索引
    private var mLastIndex = 0
    // 子View宽度
    private var mChildWidth = 0
    // 是否完成涟漪动画
    private var isFinish = true
    // 涟漪动画圆半径
    private var mRadius = 0f
    // 画笔
    private var mPaint: Paint? = null
    // 涟漪动画圆心坐标
    private var mPoint: PointF = PointF(0f, 0f)
    // 传过来的ViewPager
    private var mViewPager: ViewPager? = null
    // 数据分析fragment
    private var mFragment: AnalysisFragment? = null
    // 颜色
    private val mColors = arrayOf(Color.parseColor("#4a75ed")  //蓝
            , Color.parseColor("#76f930")        //绿
            , Color.parseColor("#f9a81e")        // 橙
            , Color.parseColor("#f5184c"))       // 红

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun init() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.isAntiAlias = true
        mPaint!!.isDither = true
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.color = mColors[mCurrentIndex]
        setWillNotDraw(false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measuredWidth
        mChildCount = childCount
        mHeight = measuredHeight

        if (mChildCount != 0) {
            mChildWidth = mWidth / mChildCount
        } else {
            mChildWidth = mWidth
        }

    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        when (ev!!.action) {
            MotionEvent.ACTION_UP -> {
                val mX = ev.x
                val mY = ev.y
                val index = getIndex(mX)
                if (index == mCurrentIndex || !isFinish)
                    return false
                if (mViewPager != null) {
                    mCurrentX = mX
                    mCurrentY = mY
                    mViewPager!!.currentItem = index
                }
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(mColors[mLastIndex])
        canvas.drawCircle(mPoint.x, mPoint.y, mRadius, mPaint)
    }

    private fun getIndex(x: Float): Int {
        return (0..mChildCount - 1).firstOrNull { x >= it * mChildWidth && x <= (it + 1) * mChildWidth }
                ?: 0
    }

    private fun getMaxLength(x: Float, y: Float): Double {
        if (x <= mWidth / 2) {
            if (y <= mHeight / 2) {
                return Math.sqrt(((mWidth - x) * (mWidth - x) + (mHeight - y) * (mHeight - y)).toDouble())
            } else {
                return Math.sqrt(((mWidth - x) * (mWidth - x) + y * y).toDouble())
            }
        } else {
            if (y <= mHeight / 2) {
                return Math.sqrt((x * x + (mHeight - y) * (mHeight - y)).toDouble())
            } else {
                return Math.sqrt((x * x + y * y).toDouble())
            }
        }
    }

    fun setViewPager(viewPager: ViewPager) {
        mViewPager = viewPager
        mViewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageSelected(position: Int) {
                setCurrentIndex(position, mCurrentX, mCurrentY)
            }
        })
    }

    fun setAnalysisFragment(fragment: AnalysisFragment) {
        mFragment = fragment
    }

    fun setCurrentIndex(index: Int, x: Float, y: Float) {
        var mX = x
        var mY = y
        if (mX < 0 && mY < 0) {
            mX = (index * mChildWidth + mChildWidth / 2).toFloat()
            mY = (mHeight / 2).toFloat()
        }
//
//        if (index == 1) {
//            if (mFragment!!.isChange) {
//                mFragment!!.reLoadData()
//                mFragment!!.isChange = false
//            }
//        }
        isFinish = false
        mLastIndex = mCurrentIndex
        mCurrentIndex = index
        mPoint.x = mX
        mPoint.y = mY
        mPaint!!.color = mColors[mCurrentIndex]
        val radius: Float = getMaxLength(mX, mY).toFloat()
        val animator = ValueAnimator.ofFloat(0f, radius)
        animator.duration = 300
        animator.start()
        animator.addUpdateListener {
            valueAnimator ->
            mRadius = valueAnimator.animatedValue as Float
            if (mRadius >= radius) {
                isFinish = true
                mCurrentX = -1f
                mCurrentY = -1f
            }
            invalidate()
        }
    }
}