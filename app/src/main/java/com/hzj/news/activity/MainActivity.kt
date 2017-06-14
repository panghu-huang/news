package com.hzj.news.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import com.hzj.news.Fragment.AnalysisFragment
import com.hzj.news.Fragment.NewsFragment
import com.hzj.news.R
import com.hzj.news.adapter.ViewPagerAdapter
import com.hzj.news.widget.BottomBar

class MainActivity : AppCompatActivity() {

    var newsFragment: NewsFragment? = null
    var analysisFragment: AnalysisFragment? = null
    private var localBroadcast: LocalBroadcastManager? = null
    private var broadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        localBroadcast = LocalBroadcastManager.getInstance(this)
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                analysisFragment!!.isChange = true
            }
        }
        val intentFilter = IntentFilter("com.hzj.news.Fragment.NewsFragment")
        localBroadcast!!.registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        localBroadcast!!.unregisterReceiver(broadcastReceiver)
    }

    private fun initViews() {
        val bottomBar = findViewById(R.id.bottom_bar) as BottomBar
        val viewPager = findViewById(R.id.vp_main) as ViewPager
        val fragments = ArrayList<Fragment>()
        newsFragment = NewsFragment()
        analysisFragment = AnalysisFragment()
        fragments.add(newsFragment!!)
        fragments.add(analysisFragment!!)
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, fragments)
        bottomBar.setViewPager(viewPager)
        bottomBar.setAnalysisFragment(analysisFragment!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_ent -> {
                newsFragment!!.loadNews(newsFragment!!.NEWS_CLASSIFY_ENT)
            }
            R.id.action_game -> {
                newsFragment!!.loadNews(newsFragment!!.NEWS_CLASSIFY_GAME)
            }
            R.id.action_country -> {
                newsFragment!!.loadNews(newsFragment!!.NEWS_CLASSIFY_COUNTRY)
            }
        }
        return true
    }
}
