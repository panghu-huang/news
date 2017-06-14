package com.hzj.news.activity

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import com.hzj.news.R

class NewsDetailByWebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail_by_web)
        initViews()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews() {
        title = "新闻详情"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val url = intent.getStringExtra("url")
        val mWvNews = findViewById(R.id.wv_news) as WebView
        mWvNews.loadUrl(url)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
