package com.hzj.news.entity

/**
 * Created by Logan on 2017/6/12.
 */
class Message {
    companion object {
        val MESSAGE_TYPE_SEND = -1
        val MESSAGE_TYPE_RECEIVE = -2
        val MESSAGE_TYPE_DIVIDER = -3
    }

    var message: String? = null
    var time: String? = null
    var type: Int? = null
}