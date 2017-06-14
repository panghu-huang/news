package com.hzj.news.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.hzj.news.utils.printLog

/**
 * Created by Logan on 2017/6/10.
 */
class ViewPagerAdapter(fm: FragmentManager, private var fragments: ArrayList<Fragment>) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment{
        printLog("getItem")
        return fragments[position]
    }

}