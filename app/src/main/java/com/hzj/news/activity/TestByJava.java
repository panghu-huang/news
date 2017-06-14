package com.hzj.news.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Logan on 2017/6/11.
 */

public class TestByJava extends SQLiteOpenHelper{
    public TestByJava(Context context) {
        super(context, "", null, 1);
    }
    private static TestByJava ins;

    public static TestByJava getIns(Context context){
        return ins;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
