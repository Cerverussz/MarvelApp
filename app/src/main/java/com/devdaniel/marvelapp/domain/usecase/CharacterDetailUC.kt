package com.devdaniel.marvelapp.domain.usecase

import com.devdaniel.marvelapp.domain.common.Result
import com.devdaniel.marvelapp.domain.model.ComicCharacterDetail
import com.devdaniel.marvelapp.domain.repository.CharacterDetailRepository

class CharacterDetailUC(private val characterDetailRepository: CharacterDetailRepository) {

    suspend fun getComicsCharacterDetail(characterId: Int): Result<ComicCharacterDetail> =
        characterDetailRepository.getComicsCharacterDetail(characterId = characterId)
}
