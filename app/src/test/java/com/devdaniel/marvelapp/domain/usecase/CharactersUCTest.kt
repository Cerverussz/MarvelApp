@file:OptIn(ExperimentalCoroutinesApi::class)

package com.devdaniel.marvelapp.domain.usecase

import com.devdaniel.marvelapp.domain.common.Result
import com.devdaniel.marvelapp.domain.model.Character
import com.devdaniel.marvelapp.domain.repository.CharactersRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CharactersUCTest {

    private val charactersRepository: CharactersRepository = mockk()

    private lateinit var charactersUC: CharactersUC

    @Before
    fun setup() {
        charactersUC = CharactersUC(charactersRepository)
    }

    @Test
    fun getRemoteCharacters() = runTest {
        val result = mockk<Result<List<Character>>>()
        coEvery { charactersRepository.getCharacters("name") } returns result

        charactersUC.getRemoteCharacters("name")

        coVerify {
            charactersRepository.getCharacters("name")
        }
    }

    @Test
    fun getLocalCharacters() = runTest {
        val characters = listOf<Character>()
        every { charactersRepository.getLocalCharacters() } returns flowOf(characters)

        charactersUC.getLocalCharacters()

        verify { charactersRepository.getLocalCharacters() }
    }
}
