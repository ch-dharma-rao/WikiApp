package com.example.wikiapp

import android.app.Application
import com.example.wikiapp.db.CategoryDatabase
import com.example.wikiapp.repository.CategoyRepository

class WikiApplication : Application() {

    lateinit var  categoyRepository: CategoyRepository

    override fun onCreate() {
        super.onCreate()
        intialize()
    }

    private fun intialize() {
        val database = CategoryDatabase.getDatabase(applicationContext)
        categoyRepository = CategoyRepository(database,applicationContext)
    }
}