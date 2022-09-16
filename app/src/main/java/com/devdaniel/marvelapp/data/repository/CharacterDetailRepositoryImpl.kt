package com.devdaniel.marvelapp.data.repository

import com.devdaniel.marvelapp.data.mappers.toCharacterDetailDomain
import com.devdaniel.marvelapp.data.remote.CharacterDetailApi
import com.devdaniel.marvelapp.domain.common.Result
import com.devdaniel.marvelapp.domain.common.fold
import com.devdaniel.marvelapp.domain.common.makeSafeRequest
import com.devdaniel.marvelapp.domain.model.CharacterDetail
import com.devdaniel.marvelapp.domain.repository.CharacterDetailRepository

class CharacterDetailRepositoryImpl(private val characterDetailApi: CharacterDetailApi) :
    CharacterDetailRepository {
    override suspend fun getCharacterDetail(characterId: Int): Result<CharacterDetail> {
        val result = makeSafeRequest { characterDetailApi.getCharacterDetail(characterId) }

        return result.fold(
            onSuccess = {
                Result.Success(it.data.infoCharacter.first().toCharacterDetailDomain())
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
