package com.example.wikiapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wikiapp.repository.ArticleRepository

class ArticlesViewModelFactory(private val repository: ArticleRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ArticlesViewModel(repository) as T
    }
}