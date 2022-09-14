package com.devdaniel.marvelapp.ui.mappers

import com.devdaniel.marvelapp.domain.model.Character
import com.devdaniel.marvelapp.ui.model.InfoCharacter

fun Character.toCharacterPresentation(): InfoCharacter {
    return InfoCharacter(
        id = id,
        name = name,
        description = description,
        thumbnail = thumbnail,
        comicAvailable = comicAvailable,
        seriesAvailable = seriesAvailable,
        storiesAvailable = storiesAvailable

    )
}
