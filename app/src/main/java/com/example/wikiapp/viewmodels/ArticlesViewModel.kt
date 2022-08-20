package com.example.wikiapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wikiapp.model.Articles
import com.example.wikiapp.repository.ArticleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticlesViewModel(private val repository: ArticleRepository) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getArticles()
        }
    }

    val articles: LiveData<Articles>
        get() = repository.articles


}