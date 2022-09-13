package com.devdaniel.marvelapp.domain.model

data class CharacterDetail(
    val id: Int,
    val name: String,
    val modified: String,
    val description: String,
    val thumbnail: String,
    val comicAvailable: Int,
    val seriesAvailable: Int,
    val storiesAvailable: Int
)
