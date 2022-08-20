package com.example.wikiapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey


data class Categories(
    val query: Query3
)

data class Query3(
    val allcategories: List<Category>
)

@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val category: String
)

