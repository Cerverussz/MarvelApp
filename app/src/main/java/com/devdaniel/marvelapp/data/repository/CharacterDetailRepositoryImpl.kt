package com.devdaniel.marvelapp.data.repository

import com.devdaniel.marvelapp.data.mappers.toComicCharacterDetailDomain
import com.devdaniel.marvelapp.data.remote.CharacterDetailApi
import com.devdaniel.marvelapp.domain.common.Result
import com.devdaniel.marvelapp.domain.common.fold
import com.devdaniel.marvelapp.domain.common.makeSafeRequest
import com.devdaniel.marvelapp.domain.model.ComicCharacterDetail
import com.devdaniel.marvelapp.domain.repository.CharacterDetailRepository

class CharacterDetailRepositoryImpl(
    private val characterDetailApi: CharacterDetailApi
) : CharacterDetailRepository {

    override suspend fun getComicsCharacterDetail(characterId: Int): Result<ComicCharacterDetail> {
        val result = makeSafeRequest { characterDetailApi.getComicsCharacterDetail(characterId) }
        return result.fold(
            onSuccess = {
                Result.Success(it.data.toComicCharacterDetailDomain())
            },
            onError = { code, message ->
                Result.Error(code, message)
            },
            onException = {
                Result.Exception(it)
            }

        )
    }
}
