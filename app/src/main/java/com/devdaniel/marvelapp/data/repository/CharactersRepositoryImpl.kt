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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class CharactersRepositoryImpl(
    private val charactersApi: CharactersApi,
    private val charactersDao: CharactersDao
) : CharactersRepository {
    override suspend fun getCharacters(name: String): Result<List<Character>> {
        val result = makeSafeRequest { charactersApi.getCharacters(name) }

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
            characters.mapToDomain()
        }
        emit(allCharacters)
    }.catch { exception ->
        println("daniel $exception")
    }

    private fun addCharacterToLocalStorage(character: CharacterDTO) {
        character.data.infoCharacter.forEach { infoCharacter ->
            val isCharacterExist = charactersDao.characterExist(characterId = infoCharacter.id)
            if (isCharacterExist.not()) {
                charactersDao.insertCharacter(infoCharacter.mapToDatabase())
            }
        }
    }
}
