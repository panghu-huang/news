package com.hzj.news.activity

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.hzj.news.R
import com.hzj.news.dao.LikeDao
import com.hzj.news.entity.Like
import com.hzj.news.entity.News
import com.hzj.news.entity.NewsDetail
import com.hzj.news.utils.LoadImage
import com.hzj.news.utils.NewsUtils
import com.hzj.news.utils.printLog
import com.hzj.news.utils.showToast
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Logan on 2017/6/11.
 */
class NewsDetailActivity : AppCompatActivity() {
    private var mTvTitle: TextView? = null
    private var mTvTime: TextView? = null
    private var mLayoutContent: LinearLayout? = null
    private var mClassify = ""
    private val mPadding = 10
    private var news: News? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        initViews()
        loadNewsData()
    }

    private fun loadNewsData() {
        val dialog = ProgressDialog(this)
        dialog.setMessage("加载中，请稍后...")
        dialog.setCancelable(false)
        dialog.show()
        val intent = intent
        NewsUtils().getBaijiaNewsDetail(news!!.url!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<NewsDetail> {
                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onComplete() = dialog.dismiss()

                    override fun onError(e: Throwable?) = printLog("onError $e")

                    override fun onNext(value: NewsDetail?) = setNewsDetailData(value!!)
                })
    }

    private fun setNewsDetailData(newsDetail: NewsDetail) {
        mTvTitle!!.text = newsDetail.title
        mTvTime!!.text = newsDetail.time

        val sb: StringBuilder = StringBuilder()
        for (newsContent in newsDetail.content!!) {
            if (newsContent.isImg!!) {
                if (sb.toString() != "") {
                    addTextView(sb.toString())
                    sb.delete(0, sb.length)
                }
                addImageView(newsContent.content!!)
            } else {
                sb.appendln(newsContent.content)
            }
        }

        if (sb.toString() != "") {
            addTextView(sb.toString())
        }
    }

    private fun addTextView(text: String) {
        val tv = TextView(this)
        tv.text = text
        tv.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        tv.setPadding(0, mPadding, 0, mPadding)
        mLayoutContent!!.addView(tv)
    }

    private fun addImageView(url: String) {
        val iv = ImageView(this)
        LoadImage(this).load(url).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Bitmap> {
                    override fun onComplete() {}

                    override fun onError(e: Throwable?) = printLog("onError $e")

                    override fun onNext(bitmap: Bitmap?) {
                        if (bitmap != null) {
                            iv.setImageBitmap(bitmap)
                        }
                    }

                    override fun onSubscribe(d: Disposable?) {
                    }
                })
        iv.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        iv.setPadding(0, mPadding, 0, mPadding)
        iv.adjustViewBounds = true
        iv.scaleType = ImageView.ScaleType.CENTER
        mLayoutContent!!.addView(iv)
    }

    private fun initViews() {
        title = "新闻详情"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mTvTitle = findViewById(R.id.tv_title) as TextView
        mTvTime = findViewById(R.id.tv_time) as TextView
        mLayoutContent = findViewById(R.id.ll_content) as LinearLayout

        news = intent.getSerializableExtra("news") as News?
        mClassify = intent.getStringExtra("classify")
        val mIvLike = findViewById(R.id.iv_like) as ImageView
        val likeDao = LikeDao.getInstance(this)
        var isExist = likeDao.isExist(news!!.title!!)
        if (isExist) {
            mIvLike.setImageResource(R.mipmap.ic_like_checked)
        }
        mIvLike.setOnClickListener {
            view ->
            if (isExist) {
                likeDao.removeLike(news!!.title!!)
                mIvLike.setImageResource(R.mipmap.ic_like_normal)
                isExist = false
                showToast("已取消喜欢")
            } else {
                val like = Like()
                like.classify = mClassify
                like.url = news!!.url
                if (news!!.isImage) {
                    like.isImg = News.NEWS_TEXT_AND_IMG
                    like.imgUrl = news!!.imgUrl
                } else {
                    like.isImg = News.NEWS_TEXT
                }
                like.title = news!!.title
                like.type = news!!.type
                likeDao.addLike(like)
                mIvLike.setImageResource(R.mipmap.ic_like_checked)
                isExist = true
                showToast("已添加到我喜欢的")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}