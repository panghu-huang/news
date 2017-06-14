package com.hzj.news.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by Logan on 2017/6/13.
 */
class MD5Utils {
    fun getMD5String(key: String): String {
        var cacheKey: String
        try {
            val mDigest = MessageDigest.getInstance("MD5")
            mDigest.update(key.toByteArray())
            cacheKey = bytesToHexString(mDigest.digest())
        } catch (e: NoSuchAlgorithmException) {
            cacheKey = key.hashCode().toString()
        }

        return cacheKey
    }

    private fun bytesToHexString(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (i in bytes.indices) {
            val hex = Integer.toHexString(0xFF and bytes[i].toInt())
            if (hex.length == 1) {
                sb.append('0')
            }
            sb.append(hex)
        }
        return sb.toString()
    }
}