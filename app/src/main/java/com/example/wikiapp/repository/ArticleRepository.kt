package com.example.wikiapp.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wikiapp.R
import com.example.wikiapp.api.WikiApiService
import com.example.wikiapp.db.ArticleDatabase
import com.example.wikiapp.model.Article
import com.example.wikiapp.model.ArticleDetails
import com.example.wikiapp.model.Articles
import com.example.wikiapp.model.Query
import com.example.wikiapp.utils.NetworkUtils
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking

class ArticleRepository(
    private val articleDatabase: ArticleDatabase,
    private val applicationContext: Context
) {

    private val wikiApiService by lazy {
        WikiApiService.create("https://en.wikipedia.org/w/")
    }

    var keys: MutableSet<String>? = null

    private var articlesLiveData = MutableLiveData<Articles>()
    val articles: LiveData<Articles>
        get() = articlesLiveData

    @SuppressLint("CheckResult")
    suspend fun getArticles() {
        var articleList: List<Article>
        var articlesList: List<Articles>

        var obj: String

        if (NetworkUtils.isInternetAvailable(applicationContext)) {

            var arrArticle: MutableMap<String, Article>? = null
            var obj: String
            var jsonObject: JsonObject
            var jsonArray: JsonArray
            var results: Articles
            var count = 0


            wikiApiService.getArticles(
                "query",
                "json",
                "random",
                "0",
                "revisions|images|description",
                "content",
                "10",
                "||"
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        result.query.pages.keys.forEach { key ->

                            var pageid: Int = key.toInt()
                            var title: String? = result.query.pages.get(key)?.title
                            var description: String? =
                                result.query.pages.get(key)?.description.toString()
                            var imageUrl: String? =
                                result.query.pages.get(key)?.images?.get(0)?.title.toString()

                            if (description == "null") {
                                description = title
                            }

                            if (imageUrl == "null") {
                                imageUrl = R.drawable.wiki_article.toString()
                            } else {
                                imageUrl =
                                    "https://commons.wikimedia.org/wiki/Special:FilePath/" + imageUrl + "?width=200"
                            }

                            Log.d("URL ARTICLE IMAGES", imageUrl)
//                articleList?.add(Article(100,pageid,title,"","article"))
//

                            var a1 = Article(pageid, pageid, title, imageUrl, description)
//                var aa : Article = (adapter.currentList)// (key.toInt(),it.query.pages.get(key))
//                aa.add(key.toInt(),a1)
//                                mutableList.add(a1)
                            runBlocking {
                                Log.d("INSIDE RUNNING", a1.toString())
                                articleDatabase.articleDao().addArticles(a1)
                            }

                        }

                        articlesLiveData.postValue(result)


                        Log.d("ARTICLE RESULT", result.query.pages.keys.toString())
                        articlesLiveData.value?.query?.pages?.toString()
                            ?.let { Log.d("ARTICLE LIST", it) }


                    },
                    { error ->
                        Log.d("ARTICE LIST ERROR", error.message.toString())
                    }
                )


        } else {
            articleList = articleDatabase.articleDao().getArticles()

            var map = articleList.associateBy(
                { it.pageID.toString() },
                {
                    var article = it
                    ArticleDetails(
                        article.id,
                        article.pageID,
                        article.title,
                        listOf(),
                        article.description
                    )
                })
            var articleList = Articles(Query(map))
        }
    }


}