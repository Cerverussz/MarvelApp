package com.devdaniel.marvelapp.domain.usecase

import com.devdaniel.marvelapp.domain.common.Result
import com.devdaniel.marvelapp.domain.model.Character
import com.devdaniel.marvelapp.domain.repository.CharactersRepository

class GetCharactersUC(private val charactersRepository: CharactersRepository) {

    suspend operator fun invoke(): Result<List<Character>> = charactersRepository.getCharacters()
}
