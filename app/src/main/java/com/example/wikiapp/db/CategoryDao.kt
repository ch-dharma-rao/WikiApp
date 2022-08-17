package com.example.wikiapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.wikiapp.model.Category

@Dao
interface CategoryDao {

    @Insert
    suspend fun addCategories(categories: List<Category>)

    @Query("SELECT * FROM category")
    suspend fun getCategories(): List<Category>


}