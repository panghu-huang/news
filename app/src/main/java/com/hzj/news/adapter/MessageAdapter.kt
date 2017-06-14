package com.hzj.news.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.hzj.news.R
import com.hzj.news.entity.Message

/**
 * Created by Logan on 2017/6/12.
 */
class MessageAdapter(context: Context, private val messages: ArrayList<Message>) : BaseAdapter() {

    private var inflater: LayoutInflater? = null

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int = messages.size

    override fun getItem(position: Int): Any = messages[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getViewTypeCount(): Int = 3

    override fun getItemViewType(position: Int): Int {
        return messages[position].type!!
    }

    override fun getView(position: Int, view: View?, container: ViewGroup): View {
        var convertView = view
        val message = messages[position]
        val holder: ViewHolder
        if (convertView == null) {
            holder = ViewHolder()
            if (message.type == Message.MESSAGE_TYPE_DIVIDER) {
                convertView = inflater!!.inflate(R.layout.item_divider, container, false)
            } else if (message.type == Message.MESSAGE_TYPE_RECEIVE) {
                convertView = inflater!!.inflate(R.layout.item_receive, container, false)
                holder.mTvMsg = convertView.findViewById(R.id.tv_msg) as TextView?
                holder.mTvTime = convertView.findViewById(R.id.tv_time) as TextView?
            } else {
                convertView = inflater!!.inflate(R.layout.item_send, container, false)
                holder.mTvMsg = convertView.findViewById(R.id.tv_msg) as TextView?
                holder.mTvTime = convertView.findViewById(R.id.tv_time) as TextView?
            }
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        if (message.type != Message.MESSAGE_TYPE_DIVIDER) {
            holder.mTvMsg!!.text = message.message
            holder.mTvTime!!.text = message.time
        }
        return convertView!!
    }

    private class ViewHolder {
        var mTvMsg: TextView? = null
        var mTvTime: TextView? = null
    }
}