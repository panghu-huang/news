package com.hzj.news.utils

import io.reactivex.Observable
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

/**
 * Created by Logan on 2017/6/12.
 */
class TulingUtils {
    private var client: OkHttpClient? = null
    val address = "http://www.tuling123.com/openapi/api"
    val key = "4376eb2dd99f0e416b72d33380860d7f"

    init {
        client = OkHttpClient()
    }

    fun talkWidthRobot(message: String): Observable<String> {
        return Observable.create {
            subscriber ->
            val requestBody = FormBody.Builder()
                    .add("key", key)
                    .add("info", message)
                    .build()
            val request = Request.Builder()
                    .url(address)
                    .post(requestBody)
                    .build()
            client!!.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    subscriber.onError(e)
                }

                override fun onResponse(call: Call?, response: Response) {
                    if (response.isSuccessful) {
                        val jsonObject = JSONObject(response.body()!!.string())
                        subscriber.onNext(jsonObject["text"].toString())
                    }
                    subscriber.onComplete()
                }
            })
        }
    }
}