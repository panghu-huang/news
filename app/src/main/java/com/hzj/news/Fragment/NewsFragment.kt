package com.hzj.news.Fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.hzj.news.R
import com.hzj.news.activity.NewsDetailActivity
import com.hzj.news.activity.NewsDetailByWebActivity
import com.hzj.news.adapter.NewsAdapter
import com.hzj.news.dao.HistoryDao
import com.hzj.news.entity.History
import com.hzj.news.entity.News
import com.hzj.news.utils.NewsUtils
import com.hzj.news.widget.TouchPullView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Logan on 2017/6/10.
 */
class NewsFragment : Fragment() {

    val NEWS_CLASSIFY_ENT = "ent"
    val NEWS_CLASSIFY_GAME = "game"
    val NEWS_CLASSIFY_COUNTRY = "country"
    private var mLvNews: ListView? = null
    private var mNews: ArrayList<News>? = null
    private var mCurrentClassify = "ent"
    private var mCurrentY = 0f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragment = inflater.inflate(R.layout.fragment_news, container, false)
        val localBroadcast = LocalBroadcastManager.getInstance(context)
        mLvNews = fragment.findViewById(R.id.lv_news) as ListView
        mLvNews!!.setOnItemClickListener { p0, p1, position, p3 ->
            if (mNews != null) {
                // position-1 是因为加了HeadView position后移
                val news = mNews!![position - 1]
                if (!news.isHeadView) {
                    localBroadcast.sendBroadcast(Intent("com.hzj.news.Fragment.NewsFragment"))
                    val history = History()
                    history.classify = mCurrentClassify
                    history.title = news.title
                    history.type = news.type
                    history.url = news.url
                    if (news.isImage) {
                        history.isImg = News.NEWS_TEXT_AND_IMG
                        history.imgUrl = news.imgUrl
                    } else {
                        history.isImg = News.NEWS_TEXT
                    }
                    HistoryDao.getInstance(context).addOrUpdateHistory(history)
                    val intent: Intent = Intent()
                    intent.putExtra("news", news)
                    if (news.type_en == "BAIJIA") {
                        intent.putExtra("classify", mCurrentClassify)
                        intent.setClass(context!!, NewsDetailActivity::class.java)
                    } else {
                        intent.setClass(context!!, NewsDetailByWebActivity::class.java)
                    }
                    startActivity(intent)
                }
            }
        }

        val touchPullView = TouchPullView(context)
        mLvNews!!.addHeaderView(touchPullView)
        mLvNews!!.setOnTouchListener { view, ev ->
            when (ev!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    mCurrentY = ev.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    val y = ev.rawY
                    if (y - mCurrentY < touchPullView.DRAG_HEIGHT_MAX
                            && y > mCurrentY)
                        touchPullView.setProgress((y - mCurrentY) / touchPullView.DRAG_HEIGHT_MAX)
                }
                MotionEvent.ACTION_UP -> {
                    touchPullView.startAnim()
                }
            }
            // 返回false，解决与OnItemClickListener的冲突
            false
        }
        mLvNews!!.overScrollMode = View.OVER_SCROLL_NEVER
        return fragment
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadNews(mCurrentClassify)
    }

    fun loadNews(classify: String) {
        val dialog = ProgressDialog(context)
        dialog.setMessage("加载中，请稍后...")
        dialog.setCancelable(false)
        val observable: Observable<ArrayList<News>>
        if (classify == NEWS_CLASSIFY_ENT) {
            observable = NewsUtils().getEntNews()
        } else if (classify == NEWS_CLASSIFY_COUNTRY) {
            observable = NewsUtils().getCountryNews()
        } else {
            observable = NewsUtils().getGameNews()
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ArrayList<News>> {
                    override fun onComplete() {
                        mLvNews?.adapter = NewsAdapter(context, mNews!!)
                        mCurrentClassify = classify
                        dialog.dismiss()
                    }

                    override fun onError(e: Throwable?) {
                        dialog.dismiss()
                        Toast.makeText(context, "加载数据出错了,看看别的新闻吧...", Toast.LENGTH_SHORT).show()
                    }

                    override fun onNext(value: ArrayList<News>?) {
                        mNews = value
                    }

                    override fun onSubscribe(d: Disposable?) {
                        dialog.show()
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        mLvNews!!.onItemClickListener = null
        mLvNews!!.setOnTouchListener(null)
        mNews = null
        mLvNews = null
    }
}