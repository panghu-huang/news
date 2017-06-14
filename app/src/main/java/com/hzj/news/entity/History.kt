package com.hzj.news.entity

/**
 * Created by Logan on 2017/6/11.
 */

class History {
    var classify: String? = null
    var type: String? = null
    var title: String? = null
    var url: String? = null
    var time: String? = null
    var isImg: Int = News.NEWS_TEXT
    var imgUrl: String? = null
    override fun toString(): String {
        return "History(classify=$classify, type=$type, title=$title, url=$url, time=$time, isImg=$isImg, imgUrl=$imgUrl)"
    }

}
