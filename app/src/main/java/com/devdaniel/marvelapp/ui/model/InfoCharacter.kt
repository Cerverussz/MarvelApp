package com.devdaniel.marvelapp.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InfoCharacter(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: String,
    val comicAvailable: Int,
    val seriesAvailable: Int,
    val storiesAvailable: Int
) : Parcelable
