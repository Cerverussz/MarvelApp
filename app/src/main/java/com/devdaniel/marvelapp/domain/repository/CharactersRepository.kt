package com.devdaniel.marvelapp.domain.repository

import com.devdaniel.marvelapp.domain.common.Result
import com.devdaniel.marvelapp.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {
    suspend fun getCharacters(): Result<List<Character>>
    fun getLocalCharacters(): Flow<List<Character>>
}
