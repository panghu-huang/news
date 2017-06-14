package com.hzj.news.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ListView
import com.hzj.news.R
import com.hzj.news.adapter.MessageAdapter
import com.hzj.news.dao.MessageDao
import com.hzj.news.entity.Message
import com.hzj.news.utils.TulingUtils
import com.hzj.news.utils.showToast
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Logan on 2017/6/12.
 */
class CustomerServiceActivity : AppCompatActivity() {

    private var mEtContent: EditText? = null
    private var mLvTalkLog: ListView? = null
    private var messageDao: MessageDao? = null
    private var adapter: MessageAdapter? = null
    private var messages: ArrayList<Message>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_service)
        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        messageDao!!.removeDivider()
        messageDao = null
        adapter = null
        messages = null
        mEtContent = null
        mLvTalkLog = null
    }

    private fun initViews() {
        title = "客服小J"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mEtContent = findViewById(R.id.et_content) as EditText?
        mLvTalkLog = findViewById(R.id.lv_talk_log) as ListView?

        // 初始化ListView数据
        messageDao = MessageDao.getInstance(this)
        messages = messageDao!!.getAllMessage()
        if (messages!!.size != 0) {
            messageDao!!.addDivider()
        }
        adapter = MessageAdapter(this, messages!!)
        mLvTalkLog!!.adapter = adapter
        mLvTalkLog!!.setSelection(adapter!!.count - 1)
        mLvTalkLog!!.overScrollMode = View.OVER_SCROLL_NEVER
    }

    fun sendMessage(view: View) {
        val message: String = mEtContent!!.text.toString()
        if (message == "") {
            showToast("请输入您的问题")
        } else {
            mEtContent!!.text = null
            val df = SimpleDateFormat.getDateTimeInstance()
            val sendMsg = Message()
            sendMsg.message = message
            sendMsg.type = Message.MESSAGE_TYPE_SEND
            sendMsg.time = df.format(Date())
            messageDao!!.addMessage(sendMsg)
            messages!!.add(sendMsg)
            adapter!!.notifyDataSetChanged()
            mLvTalkLog!!.setSelection(adapter!!.count - 1)
            TulingUtils().talkWidthRobot(message)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<String> {
                        override fun onComplete() {
                        }

                        override fun onError(e: Throwable?) {
                            showToast("交谈出错了")
                        }

                        override fun onNext(message: String) {
                            val msg = Message()
                            msg.message = message
                            msg.type = Message.MESSAGE_TYPE_RECEIVE
                            msg.time = df.format(Date())
                            messageDao!!.addMessage(msg)
                            messages!!.add(msg)
                            adapter!!.notifyDataSetChanged()
                            mLvTalkLog!!.setSelection(adapter!!.count - 1)
                        }

                        override fun onSubscribe(d: Disposable?) {
                        }
                    })
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