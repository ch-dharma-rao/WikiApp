package com.example.wikiapp.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wikiapp.api.WikiApiService
import com.example.wikiapp.model.ImageDetails
import com.example.wikiapp.model.Pictures
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PicturesRepository {

    private val wikiApiService by lazy {
        WikiApiService.create("https://commons.wikimedia.org/w/")
    }


    private var imagesLiveData = MutableLiveData<Pictures>()
    val pictures: LiveData<Pictures>
        get() = imagesLiveData

    @SuppressLint("CheckResult")
    suspend fun getImages() {
        var image: List<ImageDetails>
        var articlesList: List<ImageDetails>

        var obj: String


        wikiApiService.getImages(
            "query",
            "json",
            "categorymembers",
            "file",
            "imageinfo",
            "timestamp|Cuser|url",
            "content",
            "Category:Featured_pictures_on_Wikimedia_Commons"
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Log.d("ANIME REPO", result.query.toString())
                    imagesLiveData.postValue(result)
//                    , result.toString(), Toast.LENGTH_SHORT).show()

//                    result.query.pages.keys.forEach { key ->
//                    }
                },
                { error ->
                    Log.d("AIME LIST ERROR", error.message.toString())
                }
            )


    }

}