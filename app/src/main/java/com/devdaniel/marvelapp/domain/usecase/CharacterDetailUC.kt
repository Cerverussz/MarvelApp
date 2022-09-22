package com.devdaniel.marvelapp.domain.usecase

import com.devdaniel.marvelapp.domain.common.Result
import com.devdaniel.marvelapp.domain.model.CharacterDetail
import com.devdaniel.marvelapp.domain.repository.CharacterDetailRepository

class CharacterDetailUC(private val characterDetailRepository: CharacterDetailRepository) {
    suspend fun getCharacterDetail(characterId: Int): Result<CharacterDetail> =
        characterDetailRepository.getCharacterDetail(characterId)

    fun getLocalCharacterDetail(characterId: Int) =
        characterDetailRepository.getLocalCharacterDetail(characterId)
}
