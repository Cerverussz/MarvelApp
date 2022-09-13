package com.devdaniel.marvelapp.domain.repository

import com.devdaniel.marvelapp.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {
    fun getCharacters(): Flow<Result<List<Character>>>
}
