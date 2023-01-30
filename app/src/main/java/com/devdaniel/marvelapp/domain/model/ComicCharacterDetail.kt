package com.devdaniel.marvelapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ComicCharacterDetail(
    val infoComics: List<InfoComics>
)

@Parcelize
data class InfoComics(
    val idComic: Int,
    val description: String? = null,
    val titleComic: String,
    val thumbnail: String
) : Parcelable