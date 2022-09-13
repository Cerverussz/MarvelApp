package com.devdaniel.marvelapp.data.mappers

import com.devdaniel.marvelapp.data.model.InfoCharacter
import com.devdaniel.marvelapp.domain.model.Character
import com.devdaniel.marvelapp.domain.model.CharacterDetail

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

fun InfoCharacter.toCharacterDetailDomain(): CharacterDetail {
    return CharacterDetail(
        id = id,
        name = name,
        description = description,
        thumbnail = thumbnail.getThumbnail(),
        comicAvailable = comics.available,
        seriesAvailable = series.available,
        storiesAvailable = stories.available,
        modified = modified.take(n = 10)
    )
}

internal fun InfoCharacter.Thumbnail.getThumbnail(): String = "$path.$extension".let {
    return it.replace("http", "https")
}
