package com.hzj.news.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ListView
import com.hzj.news.R
import com.hzj.news.adapter.LikeAdapter
import com.hzj.news.dao.LikeDao

class LikeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like)
        initViews()
    }

    private fun initViews() {
        title = "我喜欢的"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val mLvLike = findViewById(R.id.lv_like) as ListView
        val likes = LikeDao.getInstance(this).getAllLike()
        if (likes.size != 0) {
            mLvLike.adapter = LikeAdapter(this, likes)
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
