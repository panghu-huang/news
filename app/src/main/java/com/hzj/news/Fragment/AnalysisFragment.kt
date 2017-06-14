package com.hzj.news.Fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.hzj.news.R
import com.hzj.news.activity.CustomerServiceActivity
import com.hzj.news.activity.HistoryActivity
import com.hzj.news.activity.LikeActivity
import com.hzj.news.adapter.AnalysisAdapter
import com.hzj.news.dao.HistoryDao
import com.hzj.news.utils.FileUtils
import com.hzj.news.widget.ListViewForScrollView
import com.hzj.news.widget.PieChart

/**
 * Created by Logan on 2017/6/10.
 */
class AnalysisFragment : Fragment(), View.OnClickListener {

    private var mPieChart: PieChart? = null
    private var mLvAnalysis: ListViewForScrollView? = null
    private var adapter: AnalysisAdapter? = null
    private var fileUtils: FileUtils? = null
    private var mTvCache: TextView? = null
    var isChange = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragment = inflater.inflate(R.layout.fragment_analysis, container, false)
        initView(fragment)
        return fragment
    }

    fun initView(fragment: View) {
        mPieChart = fragment.findViewById(R.id.pie_chart) as PieChart
        mLvAnalysis = fragment.findViewById(R.id.lv_analysis) as ListViewForScrollView
        fragment.findViewById(R.id.ll_history).setOnClickListener(this)
        fragment.findViewById(R.id.ll_kefu).setOnClickListener(this)
        fragment.findViewById(R.id.ll_like).setOnClickListener(this)
        fragment.findViewById(R.id.ll_cache).setOnClickListener(this)
        mTvCache = fragment.findViewById(R.id.tv_cache) as TextView
        fragment.findViewById(R.id.scrollView).overScrollMode = View.OVER_SCROLL_NEVER

        fileUtils = FileUtils(context)
    }

    fun reLoadData() {
        isChange = false
        val colors = arrayOf(Color.parseColor("#09338f"),
                Color.parseColor("#fd934205"), Color.parseColor("#057001"))
        val percentColors = arrayOf(Color.parseColor("#de09338f"),
                Color.parseColor("#c9934205"), Color.parseColor("#de057001"))
        val classifies = arrayOf("ent", "game", "country")
        mPieChart!!.removeAllItem()
        for (i in 0..2) {
            val map = HashMap<String, Any>()
            map.put("name", classifies[i])
            map.put("num", HistoryDao.getInstance(context).getClassifyCount(classifies[i]))
            map.put("color", colors[i])
            map.put("percentColor", percentColors[i])
            mPieChart!!.addItem(map)
        }
        mPieChart!!.drawPieChart()
        if (adapter != null) {
            adapter!!.notifyDataSetChanged()
        } else {
            adapter = AnalysisAdapter(context, mPieChart!!.getItems())
            mLvAnalysis!!.adapter = adapter
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.ll_history -> {
                startActivity(Intent(context, HistoryActivity::class.java))
            }
            R.id.ll_like -> {
                startActivity(Intent(context, LikeActivity::class.java))
            }
            R.id.ll_cache -> {
                AlertDialog.Builder(context)
                        .setTitle("清理缓存")
                        .setMessage("确定清除所有缓存？")
                        .setPositiveButton("确定") { p0, p1 ->
                            fileUtils!!.clearCache()
                            Toast.makeText(context, "清理缓存成功", Toast.LENGTH_SHORT).show()
                            mTvCache!!.text = "清理缓存(0M)"
                        }
                        .setNegativeButton("取消", null)
                        .setCancelable(false)
                        .create()
                        .show()
            }
            R.id.ll_kefu -> {
                startActivity(Intent(context, CustomerServiceActivity::class.java))
            }
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            val size = fileUtils!!.getCacheDirSize() / 1024
            var cacheSize = "$size K"
            if (size >= 1024) {
                cacheSize = "${size / 1024} M"
            }
            mTvCache!!.text = "清理缓存($cacheSize)"
            if (isChange)
                reLoadData()
        }
    }

}