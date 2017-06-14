package com.hzj.news.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.hzj.news.db.DBOpenHelper
import com.hzj.news.entity.Message
import java.util.ArrayList

/**
 * Created by Logan on 2017/6/11.
 */
/**
 * Created by Logan on 2017/6/11.
 */
class MessageDao(context: Context) {
    private val db: SQLiteDatabase = DBOpenHelper.getInstance(context).writableDatabase

    companion object {
        private var instance: MessageDao? = null
        fun getInstance(context: Context): MessageDao {
            if (instance == null) {
                instance = MessageDao(context)
            }
            return instance as MessageDao
        }
    }

    fun addMessage(message: Message) {
        val values = ContentValues()
        values.put("message", message.message)
        values.put("type", message.type)
        values.put("time", message.time)
        db.insert("message", null, values)
    }

    fun addDivider() {
        val values = ContentValues()
        values.put("type", Message.MESSAGE_TYPE_DIVIDER)
        values.put("message", "")
        values.put("time", "")
        db.insert("message", null, values)
    }

    fun removeDivider() {
        db.delete("message", "type = ?", arrayOf(Message.MESSAGE_TYPE_DIVIDER.toString()))
    }

    fun getAllMessage(): ArrayList<Message> {
        val messages = ArrayList<Message>()
        val cursor = db.query("message", null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val message = Message()
            message.type = cursor.getInt(cursor.getColumnIndex("type"))
            if (message.type != Message.MESSAGE_TYPE_DIVIDER) {
                message.message = cursor.getString(cursor.getColumnIndex("message"))
                message.time = cursor.getString(cursor.getColumnIndex("time"))
            }
            messages.add(message)
        }
        cursor.close()
        return messages
    }
}