package com.hzj.news.entity

/**
 * Created by Logan on 2017/6/11.
 */
class NewsDetail {
    var title: String? = null
    var time: String? = null
    var content: ArrayList<NewsContent>? = ArrayList()

    class NewsContent {
        var content: String? = null
        var isImg: Boolean? = false
    }
}