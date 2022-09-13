package com.devdaniel.marvelapp.domain.model

class Character(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: String,
    val comicAvailable: Int,
    val seriesAvailable: Int,
    val storiesAvailable: Int
)
