package com.devdaniel.marvelapp.domain.usecase

import com.devdaniel.marvelapp.domain.common.Result
import com.devdaniel.marvelapp.domain.model.ComicCharacterDetail
import com.devdaniel.marvelapp.domain.repository.ComicsCharacterRepository

class ComicsCharacterUC(private val characterDetailRepository: ComicsCharacterRepository) {

    suspend fun getComicsCharacterDetail(characterId: Int): Result<ComicCharacterDetail> =
        characterDetailRepository.getComicsCharacterDetail(characterId = characterId)
}
