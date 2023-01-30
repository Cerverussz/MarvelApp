package com.devdaniel.marvelapp.data.model

import com.squareup.moshi.Json

data class ComicCharacterDTO(
    @Json(name = "code")
    val code: Int,
    @Json(name = "status")
    val status: String,
    @Json(name = "copyright")
    val copyright: String,
    @Json(name = "data")
    val data: DataComic
)

data class DataComic(
    @Json(name = "results")
    val infoComics: List<InfoComics>
)

data class InfoComics(
    @Json(name = "id")
    val idComic: Int,
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "title")
    val titleComic: String,
    @Json(name = "thumbnail")
    val thumbnail: Thumbnail
)

data class Thumbnail(
    @Json(name = "path")
    val path: String,
    @Json(name = "extension")
    val extension: String
)
