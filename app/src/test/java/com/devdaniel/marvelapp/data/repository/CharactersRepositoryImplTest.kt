package com.devdaniel.marvelapp.data.repository

import com.devdaniel.marvelapp.data.local.CharactersDao
import com.devdaniel.marvelapp.data.local.entities.CharacterEntity
import com.devdaniel.marvelapp.data.mappers.mapToDatabase
import com.devdaniel.marvelapp.data.mappers.toCharacterDomain
import com.devdaniel.marvelapp.data.model.CharacterDTO
import com.devdaniel.marvelapp.data.model.Data
import com.devdaniel.marvelapp.data.model.InfoCharacter
import com.devdaniel.marvelapp.data.remote.CharactersApi
import com.devdaniel.marvelapp.domain.common.Result
import com.devdaniel.marvelapp.domain.model.Character
import com.devdaniel.marvelapp.domain.repository.CharactersRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
class CharactersRepositoryImplTest {

    private val charactersApi = mockk<CharactersApi>()
    private val charactersDao = mockk<CharactersDao>()

    private lateinit var charactersRepository: CharactersRepository

    @Before
    fun setup() {
        charactersRepository = CharactersRepositoryImpl(charactersApi, charactersDao)
    }

    @After
    fun tearDown() {
        confirmVerified(charactersApi, charactersDao)
    }

    @Test
    fun getCharactersSuccess() = runTest {
        val response = mockk<Response<CharacterDTO>>()
        val characterDTO = mockk<CharacterDTO>()
        val data = mockk<Data>()
        val infoCharacter = mockk<InfoCharacter>()
        val characters = listOf(infoCharacter)
        val character = mockk<Character>()
        every { characterDTO.data } returns data
        every { data.infoCharacter } returns characters
        every { character.id } returns 321
        every { infoCharacter.id } returns 123
        every { response.isSuccessful } returns true
        every { response.body() } returns characterDTO
        coEvery { charactersApi.getCharacters("name") } returns response
        every { charactersDao.characterExist(123) } returns false
        mockkStatic("com.devdaniel.marvelapp.data.mappers.CharacterMapperKt")
        every { charactersDao.insertCharacter(infoCharacter.mapToDatabase()) } just Runs
        every { infoCharacter.toCharacterDomain() } returns character

        val result = charactersRepository.getCharacters("name")

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data.count()).isEqualTo(1)
        assertThat((result).data.first().id).isEqualTo(321)

        verify {
            characterDTO.data
            data.infoCharacter
            character.id
            infoCharacter.id
            response.body()
            response.isSuccessful
            charactersDao.characterExist(123)
            charactersDao.insertCharacter(infoCharacter.mapToDatabase())
        }
        coVerify { charactersApi.getCharacters("name") }
    }

    @Test
    fun getCharactersSuccessNoInsertDB() = runTest {
        val response = mockk<Response<CharacterDTO>>()
        val characterDTO = mockk<CharacterDTO>()
        val data = mockk<Data>()
        val infoCharacter = mockk<InfoCharacter>()
        val characters = listOf(infoCharacter)
        val character = mockk<Character>()
        every { characterDTO.data } returns data
        every { data.infoCharacter } returns characters
        every { character.id } returns 321
        every { infoCharacter.id } returns 321
        every { response.isSuccessful } returns true
        every { response.body() } returns characterDTO
        coEvery { charactersApi.getCharacters("name") } returns response
        every { charactersDao.characterExist(321) } returns true
        mockkStatic("com.devdaniel.marvelapp.data.mappers.CharacterMapperKt")
        every { infoCharacter.toCharacterDomain() } returns character

        val result = charactersRepository.getCharacters("name")

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data.count()).isEqualTo(1)
        assertThat((result).data.first().id).isEqualTo(321)

        verify {
            characterDTO.data
            data.infoCharacter
            character.id
            infoCharacter.id
            response.body()
            response.isSuccessful
            charactersDao.characterExist(321)
        }
        coVerify { charactersApi.getCharacters("name") }
    }

    @Test
    fun getCharactersError() = runTest {
        val response = mockk<Response<CharacterDTO>>()
        every { response.isSuccessful } returns false
        every { response.body() } returns null
        every { response.code() } returns 404
        every { response.message() } returns "error"
        coEvery { charactersApi.getCharacters("name") } returns response

        val result = charactersRepository.getCharacters("name")

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).code).isEqualTo(404)
        assertThat(result.message).isEqualTo("error")

        verify {
            response.isSuccessful
            response.body()
            response.code()
            response.message()
        }
        coVerify {
            charactersApi.getCharacters("name")
        }
    }

    @Test
    fun getCharactersException() = runTest {
        val exception = UnknownHostException("error")
        coEvery { charactersApi.getCharacters("name") } throws exception

        val result = charactersRepository.getCharacters("name")

        assertThat(result).isInstanceOf(Result.Exception::class.java)
        assertThat((result as Result.Exception).exception.message).isEqualTo("error")
        coVerify {
            charactersApi.getCharacters("name")
        }
    }

    @Test
    fun getLocalCharacters() = runTest {
        val characterEntity = mockk<CharacterEntity>()
        val charactersDB = listOf(characterEntity)
        every { characterEntity.id } returns 123
        every { characterEntity.name } returns "ironman"
        every { characterEntity.modified } returns "modified"
        every { characterEntity.description } returns "description"
        every { characterEntity.thumbnail } returns "thumbnail"
        every { characterEntity.comicAvailable } returns 1
        every { characterEntity.seriesAvailable } returns 2
        every { characterEntity.storiesAvailable } returns 3
        coEvery { charactersDao.getAllCharacters() } returns charactersDB
        mockkStatic("com.devdaniel.marvelapp.data.mappers.CharacterMapperKt")

        charactersRepository.getLocalCharacters().collect { characters ->
            assertThat(characters.count()).isEqualTo(1)
            assertThat(characters.first().id).isEqualTo(123)
        }

        coVerify { charactersDao.getAllCharacters() }
        verify {
            characterEntity.id
            characterEntity.name
        }
    }
}
