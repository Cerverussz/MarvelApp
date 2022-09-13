package com.devdaniel.marvelapp.data.repository

import com.devdaniel.marvelapp.data.mappers.toCharacterDomain
import com.devdaniel.marvelapp.data.remote.CharactersApi
import com.devdaniel.marvelapp.domain.repository.CharactersRepository
import com.devdaniel.marvelapp.domain.repository.DomainExceptionRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class CharactersRepositoryImpl(
    private val charactersApi: CharactersApi,
    private val domainExceptionRepository: DomainExceptionRepository
) : CharactersRepository {

    override fun getCharacters() = flow {
        val apiResult =
            charactersApi.getCharacters().data.infoCharacter.map { it.toCharacterDomain() }
        emit(Result.success(apiResult))
    }.catch { exception ->
        Result.failure<Exception>(domainExceptionRepository.manageError(exception))
    }
}
