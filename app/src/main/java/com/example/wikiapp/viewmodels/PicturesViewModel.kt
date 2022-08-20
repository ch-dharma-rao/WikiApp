package com.example.wikiapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wikiapp.model.Pictures
import com.example.wikiapp.repository.PicturesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PicturesViewModel(private val repository: PicturesRepository) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getImages()
        }
    }

    val pictures: LiveData<Pictures>
        get() = repository.pictures


}