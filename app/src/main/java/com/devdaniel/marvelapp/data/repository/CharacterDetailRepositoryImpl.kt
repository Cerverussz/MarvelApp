package com.devdaniel.marvelapp.data.repository

import com.devdaniel.marvelapp.data.local.CharactersDao
import com.devdaniel.marvelapp.data.mappers.mapToDomainDetail
import com.devdaniel.marvelapp.data.mappers.toCharacterDetailDomain
import com.devdaniel.marvelapp.data.remote.CharacterDetailApi
import com.devdaniel.marvelapp.domain.common.Result
import com.devdaniel.marvelapp.domain.common.fold
import com.devdaniel.marvelapp.domain.common.makeSafeRequest
import com.devdaniel.marvelapp.domain.model.CharacterDetail
import com.devdaniel.marvelapp.domain.repository.CharacterDetailRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class CharacterDetailRepositoryImpl(
    private val characterDetailApi: CharacterDetailApi,
    private val charactersDao: CharactersDao
) : CharacterDetailRepository {
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

    override fun getLocalCharacterDetail(characterId: Int) = flow {
        val character = charactersDao.getCharacterBy(characterId).first()
        emit(character.mapToDomainDetail())
    }.catch { exception ->
        throw Throwable(exception.message)
    }
}
