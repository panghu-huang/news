package com.hzj.news.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.hzj.news.db.DBOpenHelper
import com.hzj.news.entity.History
import com.hzj.news.entity.News
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Logan on 2017/6/11.
 */
class HistoryDao(context: Context) {
    private val db: SQLiteDatabase = DBOpenHelper.getInstance(context).writableDatabase

    companion object {
        private var instance: HistoryDao? = null
        fun getInstance(context: Context): HistoryDao {
            if (instance == null) {
                instance = HistoryDao(context)
            }
            return instance as HistoryDao
        }
    }

    fun addOrUpdateHistory(history: History) {
        val cursor = db.query("history", null, "title = ?", arrayOf(history.title), null, null, null)
        val values = ContentValues()
        val df = SimpleDateFormat.getDateTimeInstance()
        values.put("classify", history.classify)
        values.put("type", history.type)
        values.put("title", history.title)
        values.put("url", history.url)
        values.put("time", df.format(Date()))
        values.put("isImg", history.isImg)
        if (history.isImg == News.NEWS_TEXT_AND_IMG)
            values.put("imgUrl", history.imgUrl)
        if (cursor.moveToNext()) {
            db.update("history", values, "title = ?", arrayOf(history.title))
        } else {
            db.insert("history", null, values)
        }
        cursor.close()
    }

    fun getClassifyCount(name: String): Int {
        val cursor = db.rawQuery("select count(*) from history where classify=?", arrayOf(name))
        cursor.moveToFirst()
        val count = cursor.getLong(0)
        cursor.close()
        return count.toInt()
    }

    fun getAllHistory(): ArrayList<History> {
        val historys = ArrayList<History>()
        val cursor = db.query("history", null, null, null, null, null, "_id desc")
        while (cursor.moveToNext()) {
            val history = History()
            history.classify = cursor.getString(cursor.getColumnIndex("classify"))
            history.time = cursor.getString(cursor.getColumnIndex("time"))
            history.title = cursor.getString(cursor.getColumnIndex("title"))
            history.type = cursor.getString(cursor.getColumnIndex("type"))
            history.isImg = cursor.getInt(cursor.getColumnIndex("isImg"))
            history.url = cursor.getString(cursor.getColumnIndex("url"))
            if (history.isImg == News.NEWS_TEXT_AND_IMG) {
                history.imgUrl = cursor.getString(cursor.getColumnIndex("imgUrl"))
            }
            historys.add(history)
        }
        cursor.close()
        return historys
    }
}