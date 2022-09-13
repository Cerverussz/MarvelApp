package com.devdaniel.marvelapp.domain.usecase

import com.devdaniel.marvelapp.domain.model.Character
import com.devdaniel.marvelapp.domain.repository.CharactersRepository
import kotlinx.coroutines.flow.Flow

class GetCharactersUC(private val charactersRepository: CharactersRepository) {

    operator fun invoke(): Flow<Result<List<Character>>> = charactersRepository.getCharacters()
}
