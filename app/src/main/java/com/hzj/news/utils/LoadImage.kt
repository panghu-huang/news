package com.hzj.news.utils

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.util.LruCache
import io.reactivex.Observable
import okhttp3.*
import java.io.IOException


/**
 * Created by Logan on 2017/6/10.
 */
class LoadImage(private val context: Context) {
    var client: OkHttpClient? = null
    var mLruCache: LruCache<String, Bitmap>? = null

    init {
        client = OkHttpClient()
        val memory = ((context.getSystemService(Context.ACTIVITY_SERVICE)) as ActivityManager).memoryClass
        mLruCache = object : LruCache<String, Bitmap>(memory / 8) {
            override fun sizeOf(key: String?, bitmap: Bitmap?): Int {
                // 重写此方法来衡量每张图片的大小，默认返回图片数量。
                return bitmap!!.byteCount / 1024
            }
        }
    }

    fun load(url: String): Observable<Bitmap> {
        return Observable.create {
            subscriber ->
            val fileUtils = FileUtils(context)
            var bitmap = fileUtils.getBitmapByCache(url)
            if (bitmap == null) {
                val request = Request.Builder().url(url).get().build()
                client!!.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        subscriber.onError(e)
                    }

                    override fun onResponse(call: Call?, response: Response) {
                        if (response.isSuccessful) {
                            val inputStream = response.body()!!.byteStream()
                            bitmap = BitmapFactory.decodeStream(inputStream)
                            fileUtils.addBitmapCache(bitmap!!, url)
                            subscriber.onNext(bitmap)
                            bitmap = fileUtils.getBitmapByCache(url)
                        }
                        subscriber.onComplete()
                    }
                })
            } else {
                subscriber.onNext(bitmap)
                subscriber.onComplete()
            }
        }
    }
}