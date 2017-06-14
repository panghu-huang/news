package com.hzj.news.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.hzj.news.db.DBOpenHelper
import com.hzj.news.entity.Like
import com.hzj.news.entity.News
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Logan on 2017/6/11.
 */
/**
 * Created by Logan on 2017/6/11.
 */
class LikeDao(context: Context) {
    private val db: SQLiteDatabase = DBOpenHelper.getInstance(context).writableDatabase

    companion object {
        private var instance: LikeDao? = null
        fun getInstance(context: Context): LikeDao {
            if (instance == null) {
                instance = LikeDao(context)
            }
            return instance as LikeDao
        }
    }

    fun addLike(like: Like) {
        val df = SimpleDateFormat.getDateTimeInstance()
        val values = ContentValues()
        values.put("classify", like.classify)
        values.put("type", like.type)
        values.put("title", like.title)
        values.put("url", like.url)
        values.put("time", df.format(Date()))
        values.put("isImg", like.isImg)
        values.put("imgUrl", like.imgUrl)
        db.insert("like", null, values)
    }

    fun isExist(newsTitle: String): Boolean {
        val cursor = db.query("like", null, "title = ?", arrayOf(newsTitle), null, null, null)
        val isExist = cursor.moveToNext()
        cursor.close()
        return isExist
    }

    fun removeLike(newsTitle: String) {
        db.delete("like", "title = ?", arrayOf(newsTitle))
    }

    fun getAllLike(): ArrayList<Like> {
        val likes = ArrayList<Like>()
        val cursor = db.query("like", null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val like = Like()
            like.classify = cursor.getString(cursor.getColumnIndex("classify"))
            like.time = cursor.getString(cursor.getColumnIndex("time"))
            like.title = cursor.getString(cursor.getColumnIndex("title"))
            like.type = cursor.getString(cursor.getColumnIndex("type"))
            like.isImg = cursor.getInt(cursor.getColumnIndex("isImg"))
            like.url = cursor.getString(cursor.getColumnIndex("url"))
            if (like.isImg == News.NEWS_TEXT_AND_IMG) {
                like.imgUrl = cursor.getString(cursor.getColumnIndex("imgUrl"))
            }
            likes.add(like)
        }
        cursor.close()
        return likes
    }
}