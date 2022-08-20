package com.example.wikiapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wikiapp.repository.PicturesRepository

class PicturesViewModelFactory(private val repository: PicturesRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PicturesViewModel(repository) as T
    }
}