package com.devdaniel.marvelapp.data.repository

import com.devdaniel.marvelapp.data.local.CharactersDao
import com.devdaniel.marvelapp.data.mappers.mapToDatabase
import com.devdaniel.marvelapp.data.mappers.mapToDomain
import com.devdaniel.marvelapp.data.mappers.toCharacterDomain
import com.devdaniel.marvelapp.data.model.CharacterDTO
import com.devdaniel.marvelapp.data.remote.CharactersApi
import com.devdaniel.marvelapp.domain.common.Result
import com.devdaniel.marvelapp.domain.common.Result.Success
import com.devdaniel.marvelapp.domain.common.fold
import com.devdaniel.marvelapp.domain.common.makeSafeRequest
import com.devdaniel.marvelapp.domain.model.Character
import com.devdaniel.marvelapp.domain.repository.CharactersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class CharactersRepositoryImpl(
    private val charactersApi: CharactersApi,
    private val charactersDao: CharactersDao
) : CharactersRepository {
    override suspend fun getCharacters(): Result<List<Character>> {
        val result = makeSafeRequest { charactersApi.getCharacters() }

        return result.fold(
            onSuccess = {
                addCharacterToLocalStorage(it)
                Success(
                    it.data.infoCharacter.map { infoCharacter ->
                        infoCharacter.toCharacterDomain()
                    }
                )
            },
            onError = { code, message ->
                Result.Error(code, message)
            },
            onException = {
                Result.Exception(it)
            }
        )
    }

    override fun getLocalCharacters(): Flow<List<Character>> = flow {
        val allCharacters = charactersDao.getAllCharacters().map { characters ->
            characters.map {
                it.mapToDomain()
            }
        }
        emit(allCharacters.first())
    }

    // TODO: falta obtener los datos locales y pintarlos y ver donde queda mejor, si en vista o por aca

    private suspend fun addCharacterToLocalStorage(character: CharacterDTO) {
        character.data.infoCharacter.forEach { data ->
            val characterEntity = data.mapToDatabase()
            val isCharacterExist =
                charactersDao.getAllCharacters()
                    .map { characters -> characters.contains(characterEntity) }
            if (isCharacterExist.first().not()) {
                charactersDao.insertCharacter(characterEntity)
            }
        }
    }
}
