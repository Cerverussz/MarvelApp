package com.devdaniel.marvelapp.domain.repository

import com.devdaniel.marvelapp.domain.common.Result
import com.devdaniel.marvelapp.domain.model.Character

interface CharactersRepository {
    suspend fun getCharacters(): Result<List<Character>>
}
