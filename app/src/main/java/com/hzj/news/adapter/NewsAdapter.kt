package com.hzj.news.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.hzj.news.R
import com.hzj.news.entity.News
import com.hzj.news.utils.LoadImage
import com.hzj.news.utils.printLog
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Logan on 2017/6/10.
 */
class NewsAdapter(private val context: Context, private val newses: ArrayList<News>) : BaseAdapter() {

    private val ITEM_TYPE_HEAD_VIEW = -1
    private val ITEM_TYPE_NO_IMAGE = -2
    private val ITEM_TYPE_IMAGE_AND_TEXT = -3
    private var inflater: LayoutInflater? = null

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int = newses.size

    override fun getItem(position: Int): Any = newses[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, view: View?, container: ViewGroup?): View {
        val holder: ViewHolder?
        val news = newses[position]
        var convertView = view
        val itemType = getItemViewType(position)
        if (convertView == null) {
            holder = ViewHolder()
            if (itemType == ITEM_TYPE_HEAD_VIEW) {
                convertView = inflater!!.inflate(R.layout.item_news_type, container, false)
                holder.mTvType = convertView!!.findViewById(R.id.tv_type) as TextView?
                holder.mTvTypeEn = convertView.findViewById(R.id.tv_type_en) as TextView?
            } else {
                convertView = inflater!!.inflate(R.layout.item_news, container, false)
                holder.mTvTitle = convertView!!.findViewById(R.id.tv_title) as TextView?
                holder.mIvImg = convertView.findViewById(R.id.iv_img) as ImageView?
            }
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        if (itemType == ITEM_TYPE_HEAD_VIEW) {
            holder.mTvType?.text = news.type
            holder.mTvTypeEn?.text = news.type_en
        } else {
            holder.mTvTitle!!.text = news.title
            if (itemType == ITEM_TYPE_NO_IMAGE) {
                holder.mIvImg!!.visibility = View.GONE
            } else {
                LoadImage(context).load(news.imgUrl!!).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<Bitmap> {
                            override fun onComplete() {
                            }

                            override fun onError(e: Throwable?) {
                                printLog("onError: $e")
                            }

                            override fun onNext(bitmap: Bitmap?) {
                                if (bitmap != null) {
                                    holder.mIvImg?.setImageBitmap(bitmap)
                                }
                            }

                            override fun onSubscribe(d: Disposable?) {
                            }
                        })
            }
        }
        return convertView
    }

    override fun getViewTypeCount(): Int = 3

    override fun getItemViewType(position: Int): Int {
        val news = newses[position]
        if (news.isHeadView) {
            return ITEM_TYPE_HEAD_VIEW
        } else if (news.isImage) {
            return ITEM_TYPE_IMAGE_AND_TEXT
        } else {
            return ITEM_TYPE_NO_IMAGE
        }
    }

    private class ViewHolder {
        var mTvTitle: TextView? = null
        var mIvImg: ImageView? = null
        var mTvType: TextView? = null
        var mTvTypeEn: TextView? = null
    }
}