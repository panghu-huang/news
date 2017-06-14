package com.hzj.news.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.hzj.news.R
import com.hzj.news.entity.History
import com.hzj.news.entity.News
import com.hzj.news.utils.LoadImage
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Logan on 2017/6/13.
 */
class HistoryAdapter(private val context: Context, private val historys: ArrayList<History>) : BaseAdapter() {
    private var inflater: LayoutInflater? = null

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int = historys.size

    override fun getItem(position: Int): Any = historys[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, view: View?, container: ViewGroup): View {
        var convertView = view
        val holder: ViewHolder
        if (convertView == null) {
            convertView = inflater!!.inflate(R.layout.item_history, container, false)
            holder = ViewHolder()
            holder.mIvImg = convertView.findViewById(R.id.iv_img) as ImageView?
            holder.mTvTitle = convertView.findViewById(R.id.tv_title) as TextView?
            holder.mTvType = convertView.findViewById(R.id.tv_type) as TextView?
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val history = historys[position]
        if (history.isImg == News.NEWS_TEXT_AND_IMG) {
            LoadImage(context).load(history.imgUrl!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : Observer<Bitmap> {
                        override fun onComplete() {
                        }

                        override fun onError(e: Throwable?) {
                            Toast.makeText(context, "加载图片出错啦..", Toast.LENGTH_SHORT).show()
                        }

                        override fun onNext(value: Bitmap) {
                            holder.mIvImg!!.setImageBitmap(value)
                        }

                        override fun onSubscribe(d: Disposable?) {
                        }
                    })
        } else {
            holder.mIvImg!!.visibility = View.GONE
        }
        holder.mTvTitle!!.text = history.title
        holder.mTvType!!.text = "${history.classify}(${history.type})"
        return convertView!!
    }

    private class ViewHolder {
        var mIvImg: ImageView? = null
        var mTvTitle: TextView? = null
        var mTvType: TextView? = null
    }
}