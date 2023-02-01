package com.devdaniel.marvelapp.domain.repository

import com.devdaniel.marvelapp.domain.common.Result
import com.devdaniel.marvelapp.domain.model.ComicCharacterDetail

interface ComicsCharacterRepository {
    suspend fun getComicsCharacterDetail(characterId: Int): Result<ComicCharacterDetail>
}
