package com.devdaniel.marvelapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "character", primaryKeys = ["character_id"])
data class CharacterEntity(
    @ColumnInfo(name = "character_id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "modified")
    val modified: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "thumbnail")
    val thumbnail: String,
    @ColumnInfo(name = "comic_available")
    val comicAvailable: Int,
    @ColumnInfo(name = "series_available")
    val seriesAvailable: Int,
    @ColumnInfo(name = "stories_available")
    val storiesAvailable: Int
)
