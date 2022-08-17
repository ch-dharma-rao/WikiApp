package com.example.wikiapp.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wikiapp.api.WikiApiService
import com.example.wikiapp.db.CategoryDatabase
import com.example.wikiapp.model.Categories
import com.example.wikiapp.model.Category
import com.example.wikiapp.model.Query
import com.example.wikiapp.utils.NetworkUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking

class CategoyRepository(private val categoryDatabase: CategoryDatabase, private val applicationContext: Context) {

    private val wikiApiService by lazy {
        WikiApiService.create()
    }

    private var categoriesLiveData = MutableLiveData<Categories>()
    val categories :LiveData<Categories>
    get() = categoriesLiveData

    @SuppressLint("CheckResult")
    suspend fun getCategories(){
        var  categories: List<Category>

        if(NetworkUtils.isInternetAvailable(applicationContext)){

            wikiApiService.getCategories("query", "allcategories","List of" ,"2", "json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        categories= result.query.allcategories
                        runBlocking {
                            categoryDatabase.categoryDao().addCategories(categories)
                        }

                        categoriesLiveData.postValue(result)

                        Log.d("LIST", categoriesLiveData.value?.query?.allcategories.toString())},
                    { error ->   Log.d("CATEGORY LIST", error.message.toString())
                    }
                )
        }else{
             categories = categoryDatabase.categoryDao().getCategories()
            var categoryList = Categories(Query(categories))
            categoriesLiveData.postValue(categoryList)
        }


    }
}


