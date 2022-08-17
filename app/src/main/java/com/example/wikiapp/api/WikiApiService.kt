package com.example.wikiapp.api

import com.example.wikiapp.model.Categories
import com.example.wikiapp.model.Category
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WikiApiService {
    @GET("api.php")
    fun getCategories(@Query("action") action: String,
                      @Query("list") list: String,
                      @Query("acprefix") acprefix : String,
                      @Query("formatversion") formatversion: String,
                      @Query("format") format: String): Observable<Categories>


    companion object {
        @JvmStatic  fun create(): WikiApiService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://en.wikipedia.org/w/")
                .build()

            return retrofit.create(WikiApiService::class.java)
        }
    }
}