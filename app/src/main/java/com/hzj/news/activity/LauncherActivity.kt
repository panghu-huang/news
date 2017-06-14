package com.hzj.news.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import com.hzj.news.R

class LauncherActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        Thread(Runnable {
            SystemClock.sleep(2000)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }).start()
    }
}
