package com.hzj.news.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.hzj.news.R
import com.hzj.news.widget.SingleLineChart

/**
 * Created by Logan on 2017/6/12.
 */
class AnalysisAdapter(context: Context, private val data: ArrayList<HashMap<String, Any>>) : BaseAdapter() {
    private var inflater: LayoutInflater? = null

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int = data.size

    override fun getItem(position: Int): Any = data[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, view: View?, container: ViewGroup): View {
        var convertView = view
        val holder: ViewHolder
        if (convertView == null) {
            convertView = inflater!!.inflate(R.layout.item_analysis, container, false)
            holder = ViewHolder()
            holder.mLineChart = convertView.findViewById(R.id.line_chart) as SingleLineChart
            holder.mTvName = convertView.findViewById(R.id.tv_name) as TextView
            holder.mTvPercent = convertView.findViewById(R.id.tv_percent) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val map = data[position]
        val percent = "${map["num"]}(${((map["percent"] as Float) * 100).toInt()}%)"
        holder.mTvName!!.text = map["name"].toString()
        holder.mTvPercent!!.text = percent
        holder.mLineChart!!.setData(map)
        return convertView!!
    }

    private class ViewHolder {
        var mLineChart: SingleLineChart? = null
        var mTvName: TextView? = null
        var mTvPercent: TextView? = null
    }
}