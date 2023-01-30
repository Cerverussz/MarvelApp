package com.devdaniel.marvelapp.domain.repository

import com.devdaniel.marvelapp.domain.common.Result
import com.devdaniel.marvelapp.domain.model.ComicCharacterDetail

interface CharacterDetailRepository {
    suspend fun getComicsCharacterDetail(characterId: Int): Result<ComicCharacterDetail>
}
