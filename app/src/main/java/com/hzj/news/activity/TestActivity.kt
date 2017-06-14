package com.hzj.news.activity

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.hzj.news.R
import com.hzj.news.utils.FileUtils
import com.hzj.news.utils.LoadImage
import com.hzj.news.utils.printLog
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TestActivity : AppCompatActivity() {

    val path1 = "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg"
    val path2 = "http://i2.sanwen.net/doc/1608/704-160PQ43458.png"
    var fileUtils: FileUtils? = null
    var iv: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        iv = findViewById(R.id.iv_test) as ImageView
        fileUtils = FileUtils(this)
    }

    fun download1(view: View) {
        loadImage(path1)
        printLog("download1")
    }

    fun download2(view: View) {
        loadImage(path2)
        printLog("download2")
    }

    fun delete1(view: View) {
        printLog("delete1" + fileUtils!!.deleteBitmapFromCache(path1))
    }

    fun delete2(view: View) {
        printLog("delete2" + fileUtils!!.deleteBitmapFromCache(path2))
    }

    fun getCacheSize(view: View) {
        printLog("Cache size is ${fileUtils!!.getCacheDirSize() / 1024}K")
    }

    fun clear(view: View) {
        fileUtils!!.clearCache()
        printLog("clear")
    }

    private fun loadImage(url: String) {
        LoadImage(this).load(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Observer<Bitmap> {
                    override fun onComplete() {
                    }

                    override fun onError(e: Throwable?) {
                        printLog("e is $e")
                    }

                    override fun onNext(value: Bitmap?) {
                        if (value == null) {
                            printLog("---null---")
                        } else {
                            iv!!.setImageBitmap(value)
                            printLog("---not null---")
                        }
                    }

                    override fun onSubscribe(d: Disposable?) {
                    }
                })
    }
}
