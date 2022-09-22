package com.devdaniel.marvelapp.domain.repository

import com.devdaniel.marvelapp.domain.common.Result
import com.devdaniel.marvelapp.domain.model.CharacterDetail
import kotlinx.coroutines.flow.Flow

interface CharacterDetailRepository {
    suspend fun getCharacterDetail(characterId: Int): Result<CharacterDetail>
    fun getLocalCharacterDetail(characterId: Int): Flow<CharacterDetail>
}
