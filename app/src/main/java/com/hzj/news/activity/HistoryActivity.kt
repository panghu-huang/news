package com.hzj.news.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.ListView
import com.hzj.news.R
import com.hzj.news.adapter.HistoryAdapter
import com.hzj.news.dao.HistoryDao

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        initViews()
    }

    private fun initViews() {
        title = "浏览历史"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val mLvHistory = findViewById(R.id.lv_history) as ListView
        val historys = HistoryDao.getInstance(this).getAllHistory()
        if (historys.size != 0) {
            mLvHistory.adapter = HistoryAdapter(this, historys)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
