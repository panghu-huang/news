package com.hzj.news.entity

import java.io.Serializable

/**
 * Created by Logan on 2017/6/6.
 */
class News : Serializable {
    companion object {
        val NEWS_TEXT = 1
        val NEWS_TEXT_AND_IMG = 2
    }

    var title: String? = null
    var isImage: Boolean = false
    var imgUrl: String? = null
    var url: String? = null
    var author: String? = null
    var time: String? = null
    var type_en: String? = null
    var type: String? = null
    var isHeadView = false
}
