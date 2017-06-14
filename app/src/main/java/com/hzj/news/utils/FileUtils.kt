package com.hzj.news.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

/**
 * Created by Logan on 2017/6/13.
 */
class FileUtils(private val context: Context) {

    fun addBitmapCache(bitmap: Bitmap, url: String) {
        // 将图片写入缓存目录
        val fileName = getImageName(url)
        val file = File(getBitmapCacheDir(context),
                fileName)
        val fos = FileOutputStream(file)
        if (getImageFormat(url).toUpperCase() == "PNG") {
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos)
        } else {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
        }
        fos.flush()
        fos.close()
    }

    fun getBitmapByCache(url: String): Bitmap? {
        val fileName = getImageName(url)
        val filePath = getBitmapCacheDir(context) + File.separator + fileName
        val file = File(filePath)
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(filePath)
            return bitmap
        }
        return null
    }

    // 获取图片缓存目录
    private fun getBitmapCacheDir(context: Context): String {
        var cachePath: String
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
                || !Environment.isExternalStorageRemovable()) {
            // 外置SD卡已经挂载，或者没有移除
            cachePath = context.externalCacheDir.path
        } else {
            // 使用内置存储空间中的缓存目录
            cachePath = context.cacheDir.path
        }
        // File.separator --> "/" 目录分隔符
        cachePath = cachePath + File.separator + "bitmap"
        val cachaDir = File(cachePath)
        if (!cachaDir.exists()) {
            cachaDir.mkdir()
        }
        return cachePath
    }

    // 获取图片格式
    private fun getImageName(url: String): String {
        return MD5Utils().getMD5String(url) + "." + getImageFormat(url)
    }

    // 获取图片格式
    private fun getImageFormat(url: String): String {
        val str = url.split(".")
        return str[str.size - 1]
    }

    fun deleteBitmapFromCache(url: String): Boolean {
        val fileName = getBitmapCacheDir(context) + File.separator + getImageName(url)
        val file = File(fileName)
        return file.delete()
    }

    fun clearCache() {
        val dirName = getBitmapCacheDir(context)
        val dir = File(dirName)
        if (dir.exists()) {
            for (file in dir.listFiles()) {
                file.delete()
            }
        }
    }

    fun getCacheDirSize(): Long {
        val dirName = getBitmapCacheDir(context)
        return getFolderSize(File(dirName))
    }

    private fun getFolderSize(dir: File): Long {
        var size: Long = 0
        val files = dir.listFiles()
        if (dir.exists()) {
            for (file in files) {
                if (file.isDirectory) {
                    size += getFolderSize(file)
                } else {
                    size += file.length()
                }
            }
        }
        return size
    }
}