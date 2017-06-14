package com.hzj.news.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by Logan on 2017/6/11.
 */
class DBOpenHelper(context: Context?) : SQLiteOpenHelper(context, "news.db", null, 1) {

    companion object {
        private var instance: DBOpenHelper? = null
        fun getInstance(context: Context): DBOpenHelper {
            if (instance == null) {
                instance = DBOpenHelper(context)
            }
            val ins = instance
            return ins!!
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_HISTORY = "create table history(" +
                "_id integer primary key autoincrement," +
                "classify text not null," +
                "isImg integer," +
                "imgUrl text," +
                "type text not null," +
                "title text not null," +
                "url text not null," +
                "time text not null)"
        val CREATE_TABLE_LIKE = "create table like(" +
                "_id integer primary key autoincrement," +
                "classify text not null," +
                "isImg integer," +
                "imgUrl text," +
                "type text not null," +
                "title text not null," +
                "url text not null," +
                "time text not null)"
        val CREATE_TABLE_MESSAGE = "create table message(" +
                "_id integer primary key autoincrement," +
                "message text not null," +
                "type integer not null," +
                "time text not null)"
        db?.execSQL(CREATE_TABLE_HISTORY)
        db?.execSQL(CREATE_TABLE_LIKE)
        db?.execSQL(CREATE_TABLE_MESSAGE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}