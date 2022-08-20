package com.example.wikiapp.api

import android.util.Log
import com.example.wikiapp.model.Articles
import com.example.wikiapp.model.Categories
import com.example.wikiapp.model.Pictures
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WikiApiService {
    @GET("api.php")
    fun getCategories(
        @Query("action") action: String,
        @Query("list") list: String,
        @Query("acprefix") acprefix: String,
        @Query("formatversion") formatversion: String,
        @Query("format") format: String
    ): Observable<Categories>


    @GET("api.php")
    fun getArticles(
        @Query("action") action: String,
        @Query("format") format: String,
        @Query("generator") generator: String,
        @Query("grnnamespace") grnnamespace: String,
        @Query("prop") prop: String,
        @Query("rvprop") rvprop: String,
        @Query("grnlimit") grnlimit: String,
        @Query("continue-") continues: String
    ): Observable<Articles>

    @GET("api.php")
    fun getImages(
        @Query("action") action: String,
        @Query("format") format: String,
        @Query("generator") generator: String,
        @Query("gcmtype") gcmtype: String,
        @Query("prop") prop: String,
        @Query("iiprop") iiprop: String,
        @Query("rvprop") rvprop: String,
        @Query("gcmtitle") gcmtitle: String
    ): Observable<Pictures>


    companion object {
        @JvmStatic
        fun create(BASE_URL: String): WikiApiService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            Log.d("IMAGE LIST WIKISERV", BASE_URL)

            return retrofit.create(WikiApiService::class.java)
        }
    }


}