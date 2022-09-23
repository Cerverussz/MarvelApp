package com.devdaniel.marvelapp.domain.usecase

import com.devdaniel.marvelapp.domain.common.Result
import com.devdaniel.marvelapp.domain.model.Character
import com.devdaniel.marvelapp.domain.repository.CharactersRepository
import kotlinx.coroutines.flow.Flow

class CharactersUC(private val charactersRepository: CharactersRepository) {

    suspend fun getRemoteCharacters(): Result<List<Character>> =
        charactersRepository.getCharacters()

    fun getLocalCharacters(): Flow<List<Character>> = charactersRepository.getLocalCharacters()
}
