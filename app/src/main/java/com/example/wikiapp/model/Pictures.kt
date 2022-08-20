package com.example.wikiapp.model


data class Pictures(
    val query: Query2
)

data class Query2(val pages: Map<String, ImageDetails>)

data class ImageDetails(
    val title: String
)

data class Imageinfo(
    val descriptionshorturl: String,
    val descriptionurl: String,
    val timestamp: String,
    val url: String,
    val user: String
)