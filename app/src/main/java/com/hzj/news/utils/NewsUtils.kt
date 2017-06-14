package com.hzj.news.utils

import com.hzj.news.entity.News
import com.hzj.news.entity.NewsDetail
import io.reactivex.Observable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

/**
 * Created by Logan on 2017/6/6.
 */
class NewsUtils {
    // 娱乐新闻地址
    val PATH_NEWS_ENT = "http://news.baidu.com/ent"
    val PATH_NEWS_GAME = "http://news.baidu.com/game"
    val PATH_NEWS_COUNTRY = "http://news.baidu.com/guonei"

    fun getEntNews(): Observable<ArrayList<News>> {
        return Observable.create({ subscriber ->
            try {
                val document: Document = Jsoup.connect(PATH_NEWS_ENT).get()
                val newses = ArrayList<News>()
                // 百家专栏
                val baijiaEle: Element = document.getElementById("baijia-recommend")
                val topicEles: Elements = baijiaEle.getElementsByClass("topic")
                val baijiaNews = News()
                baijiaNews.isHeadView = true
                baijiaNews.type = "百家专栏"
                baijiaNews.type_en = "BAIJIA"
                newses.add(baijiaNews)
                for (topic in topicEles) {
                    val picture: String = topic.getElementsByClass("picture")[0]
                            .getElementsByTag("img")[0].attr("src")
                    val title: String = topic.getElementsByClass("title")[0].text()
                    val url: String = topic.getElementsByClass("title")[0]
                            .getElementsByTag("a")[0].attr("href")
                    val author: String = topic.getElementsByClass("infos")[0].text()
                    val news = News()
                    news.title = title
                    news.url = url
                    news.isImage = true
                    news.imgUrl = picture
                    news.author = author
                    news.type = "百家专栏"
                    news.type_en = "BAIJIA"
                    newses.add(news)
                }
                // 即时新闻
                val modEle: Element = document.getElementsByClass("mod")[0]
                        .getElementsByClass("ulist")[0]
                val li: Elements = modEle.getElementsByTag("li")
                val instantNews = News()
                instantNews.isHeadView = true
                instantNews.type = "即时新闻"
                instantNews.type_en = "INSTANT NEWS"
                newses.add(instantNews)
                li
                        .map { it.getElementsByTag("a")[0] }
                        .forEach {
                            val news = News()
                            news.title = it.text()
                            news.url = it.attr("href")
                            news.isImage = false
                            news.type = "即时新闻"
                            news.type_en = "INSTANT NEWS"
                            newses.add(news)
                        }
                // 焦点新闻
                val bLeftEle: Element = document.getElementsByClass("b-left")[0]
                val uLists: Elements = bLeftEle.getElementsByClass("ulist")
                val focaltNews = News()
                focaltNews.isHeadView = true
                focaltNews.type = "焦点新闻"
                focaltNews.type_en = "FOCAL NEWS"
                newses.add(focaltNews)
                uLists
                        .map { it.getElementsByTag("li") }
                        .flatMap { it }
                        .map { it.getElementsByTag("a")[0] }
                        .forEach {
                            val news = News()
                            news.title = it.text()
                            news.url = it.attr("href")
                            news.type = "焦点新闻"
                            news.type_en = "FOCAL NEWS"
                            news.isImage = false
                            newses.add(news)
                        }
                subscriber.onNext(newses)
            } catch (e: Exception) {
                subscriber.onError(e)
            } finally {
                subscriber.onComplete()
            }
        })
    }

    fun getGameNews(): Observable<ArrayList<News>> {
        return Observable.create {
            sub ->
            try {
                val newses = ArrayList<News>()
                val doc = Jsoup.connect(PATH_NEWS_GAME).get()
                val types = arrayOf("网络游戏", "电子竞技", "热门游戏", "魔兽世界")
                val typesEn = arrayOf("ONLINE GAME", "ESPORTS", "TOP GAME", "WORLD OF WARCRAFT")
                for (i in 0..types.size - 1) {
                    val sections = doc.select(".auto-fix-picText")
                    val eSports = sections[i]
                    val eSportsTitle = News()
                    if (eSports.select(".three-pic-pics").size == 0)
                        continue
                    val threePicPics = eSports.select(".three-pic-pics")[0]
                    eSportsTitle.isHeadView = true
                    eSportsTitle.type = types[i]
                    eSportsTitle.type_en = typesEn[i]
                    newses.add(eSportsTitle)
                    val classNames = arrayOf(".fl", ".three-pic-pics-center", ".fr")
                    for (className in classNames) {
                        // 第一张图片新闻信息
                        val news = News()
                        news.isImage = true
                        news.type = types[i]
                        news.type_en = typesEn[i]
                        val element = threePicPics.select(className)[0]
                        news.imgUrl = element.select("a")[0].select("img")[0].attr("src")
                        news.title = element.select("a")[1].text()
                        news.url = element.select("a")[1].attr("href")
                        newses.add(news)
                    }

                    val threePicTexts = eSports.select(".three-pic-text")[0]
                    val textWs = threePicTexts.select(".textW")
                    for (textW in textWs) {
                        val lis = textW.select("li")
                        for (li in lis) {
                            val a = li.select("a")[0]
                            val news: News = News()
                            news.isImage = false
                            news.type = types[i]
                            news.type_en = typesEn[i]
                            news.title = a.text()
                            news.url = a.attr("href")
                            newses.add(news)
                        }
                    }
                }
                sub.onNext(newses)
            } catch (e: Exception) {
                sub.onError(e)
            } finally {
                sub.onComplete()
            }

        }
    }

    fun getBaijiaNewsDetail(url: String): Observable<NewsDetail> {
        return Observable.create {
            sub ->
            try {
                val newsDetail: NewsDetail = NewsDetail()
                val doc = Jsoup.connect(url).get()
                val article = doc.select(".article")[0]
                val info = article.select(".info")[0]
                newsDetail.title = info.select(".title")[0].text()
                newsDetail.time = info.select(".time")[0].text()
                val ps = article.select(".news-content")[0].getElementsByTag("p")
                for (p in ps) {
                    val content = NewsDetail.NewsContent()
                    if (p.select("img").size != 0) {
                        content.isImg = true
                        content.content = p.select("img")[0].attr("src")
                    } else {
                        content.content = p.text()
                    }
                    newsDetail.content!!.add(content)
                }
                sub.onNext(newsDetail)
            } catch (e: Exception) {
                sub.onError(e)
            } finally {
                sub.onComplete()
            }
        }
    }

    fun getCountryNews(): Observable<ArrayList<News>> {
        return Observable.create {
            subscriber ->
            try {
                val document: Document = Jsoup.connect(PATH_NEWS_COUNTRY).get()
                val newses = ArrayList<News>()
                // 即时新闻
                val modEle: Element = document.getElementsByClass("mod")[0]
                        .getElementsByClass("ulist")[0]
                val li: Elements = modEle.getElementsByTag("li")
                val instantNews = News()
                instantNews.isHeadView = true
                instantNews.type = "即时新闻"
                instantNews.type_en = "INSTANT NEWS"
                newses.add(instantNews)
                li
                        .map { it.getElementsByTag("a")[0] }
                        .forEach {
                            val news = News()
                            news.title = it.text()
                            news.url = it.attr("href")
                            news.isImage = false
                            news.type = "即时新闻"
                            news.type_en = "INSTANT NEWS"
                            newses.add(news)
                        }
                // 焦点新闻
                val bLeftEle: Element = document.getElementsByClass("b-left")[0]
                val uLists: Elements = bLeftEle.getElementsByClass("ulist")
                val focaltNews = News()
                focaltNews.isHeadView = true
                focaltNews.type = "焦点新闻"
                focaltNews.type_en = "FOCAL NEWS"
                newses.add(focaltNews)
                uLists
                        .map { it.getElementsByTag("li") }
                        .flatMap { it }
                        .map { it.getElementsByTag("a")[0] }
                        .forEach {
                            val news = News()
                            news.title = it.text()
                            news.url = it.attr("href")
                            news.type = "焦点新闻"
                            news.type_en = "FOCAL NEWS"
                            news.isImage = false
                            newses.add(news)
                        }
                subscriber.onNext(newses)
            } catch (e: Exception) {
                subscriber.onError(e)
            } finally {
                subscriber.onComplete()
            }
        }
    }
}