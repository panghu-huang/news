package com.hzj.news.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

/**
 * Created by Logan on 2017/6/10.
 */
fun printLog(text: String) {
    Log.e("TAG", text)
}

fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}