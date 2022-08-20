package com.example.wikiapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wikiapp.model.Categories
import com.example.wikiapp.repository.CategoyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriesViewModel(private val repository: CategoyRepository) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCategories()
        }
    }

    val categories: LiveData<Categories>
        get() = repository.categories


}

