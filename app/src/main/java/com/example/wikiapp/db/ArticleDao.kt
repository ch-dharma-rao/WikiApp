package com.example.wikiapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.wikiapp.model.Article

@Dao
interface ArticleDao {

    @Insert
    suspend fun addArticles(articles: Article)

    @Query("SELECT * FROM article")
    suspend fun getArticles(): List<Article>


}