package com.example.wikiapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.JsonObject


//data class Articles(val query: Query1)
data class Query1(val pages: JsonObject)


data class Articles(
    val query: Query
)

data class Query(
    val pages: Map<String, ArticleDetails>
)

data class ImagesUrl(
    val ns: String,
    val title: String
)

data class ArticleDetails(
    val id: Int,
    val pageID: Int?,
    val title: String?,
//    val images : ImagesUrl,
    val images: List<ImagesUrl>,
    var description: String?
)

@Entity(tableName = "article")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val pageID: Int?,
    val title: String?,
    val imgUrl: String?,
    var description: String?
)

