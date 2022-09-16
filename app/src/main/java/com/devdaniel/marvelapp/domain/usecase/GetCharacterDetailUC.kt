package com.devdaniel.marvelapp.domain.usecase

import com.devdaniel.marvelapp.domain.common.Result
import com.devdaniel.marvelapp.domain.model.CharacterDetail
import com.devdaniel.marvelapp.domain.repository.CharacterDetailRepository

class GetCharacterDetailUC(private val characterDetailRepository: CharacterDetailRepository) {
    suspend operator fun invoke(characterId: Int): Result<CharacterDetail> =
        characterDetailRepository.getCharacterDetail(characterId)
}
