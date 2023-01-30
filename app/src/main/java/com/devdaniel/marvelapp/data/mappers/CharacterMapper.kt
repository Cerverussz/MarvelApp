package com.devdaniel.marvelapp.data.mappers

import com.devdaniel.marvelapp.data.local.entities.CharacterEntity
import com.devdaniel.marvelapp.data.model.DataComic
import com.devdaniel.marvelapp.data.model.InfoCharacter
import com.devdaniel.marvelapp.domain.model.Character
import com.devdaniel.marvelapp.domain.model.ComicCharacterDetail
import com.devdaniel.marvelapp.domain.model.InfoComics

fun InfoCharacter.toCharacterDomain(): Character {
    return Character(
        id = id,
        name = name,
        description = description,
        thumbnail = thumbnail.getThumbnail(),
        comicAvailable = comics.available,
        seriesAvailable = series.available,
        storiesAvailable = stories.available
    )
}

fun DataComic.toComicCharacterDetailDomain(): ComicCharacterDetail {
    return ComicCharacterDetail(
        infoComics = infoComics.map { comic ->
            InfoComics(
                idComic = comic.idComic,
                description = comic.description,
                titleComic = comic.titleComic,
                thumbnail = comic.thumbnail.getThumbnail()
            )
        }
    )
}

fun InfoCharacter.mapToDatabase(): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        modified = modified,
        description = description,
        thumbnail = thumbnail.getThumbnail(),
        comicAvailable = comics.available,
        seriesAvailable = series.available,
        storiesAvailable = stories.available
    )
}

fun CharacterEntity.mapToDomain(): Character {
    return Character(
        id = id,
        name = name,
        description = description,
        thumbnail = thumbnail,
        comicAvailable = comicAvailable,
        seriesAvailable = comicAvailable,
        storiesAvailable = storiesAvailable
    )
}

internal fun InfoCharacter.Thumbnail.getThumbnail(): String = "$path.$extension".let {
    return it.replace("http", "https")
}

internal fun com.devdaniel.marvelapp.data.model.Thumbnail.getThumbnail(): String =
    "$path.$extension".let {
        return it.replace("http", "https")
    }
