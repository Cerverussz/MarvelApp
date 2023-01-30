package com.devdaniel.marvelapp.ui.characters

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devdaniel.marvelapp.R
import com.devdaniel.marvelapp.domain.common.Result
import com.devdaniel.marvelapp.domain.model.Character
import com.devdaniel.marvelapp.domain.usecase.CharactersUC
import com.devdaniel.marvelapp.rule.CoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.io.IOException
import java.lang.reflect.Field
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CharactersViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val charactersUC = mockk<CharactersUC>(relaxed = true)

    private lateinit var charactersViewModel: CharactersViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        charactersViewModel = CharactersViewModel(charactersUC, UnconfinedTestDispatcher())
    }

    @Before
    fun tearDown() {
        confirmVerified(charactersUC)
    }

    @Test
    fun getCharactersState() {
        assertThat(
            charactersViewModel.charactersState.value
        ).isInstanceOf(CharactersState::class.java)
        assertThat(charactersViewModel.charactersState.value).isEqualTo(CharactersState.Loading)
    }

    @Test
    fun getCharactersSuccess() = runTest {
        val character = mockk<Character>()
        val characters = listOf(character)
        coEvery { charactersUC.getRemoteCharacters("name") } returns Result.Success(characters)

        val results = arrayListOf<CharactersState>()

        val job = launch(UnconfinedTestDispatcher()) {
            charactersViewModel.charactersState.toList(results)
        }

        charactersViewModel.getCharacters("name")

        assertNotNull(results)
        assertThat(results[0]).isEqualTo(CharactersState.Loading)

        coVerify { charactersUC.getRemoteCharacters("name") }
        job.cancel()
    }

    @Test
    fun getCharactersByNameSuccess() = runTest {
        val currentCharacters: Field =
            charactersViewModel.javaClass.getDeclaredField("currentCharacters").apply {
                isAccessible = true
            }
        val mainCharacters: Field =
            charactersViewModel.javaClass.getDeclaredField("mainCharacters").apply {
                isAccessible = true
            }

        val currentCharacter = mockk<Character>()
        val character = mockk<Character>()
        val characters = listOf(character)
        val totalCharacters = listOf(currentCharacter, character)

        mainCharacters.set(charactersViewModel, listOf("name"))
        currentCharacters.set(charactersViewModel, arrayListOf(currentCharacter))
        every { character.name } returns "name"
        every { character.id } returns 123
        every { currentCharacter.name } returns "name1"
        every { currentCharacter.id } returns 1232
        coEvery { charactersUC.getRemoteCharacters("name") } returns Result.Success(characters)

        val results = arrayListOf<CharactersState>()

        val job = launch(UnconfinedTestDispatcher()) {
            charactersViewModel.charactersState.toList(results)
        }

        charactersViewModel.getCharactersByName()

        assertNotNull(results)
        assertThat(results[0]).isEqualTo(CharactersState.Loading)
        assertThat(results[1]).isEqualTo(CharactersState.CharactersSuccess(totalCharacters))

        coVerify { charactersUC.getRemoteCharacters("name") }
        coVerify {
            charactersUC.getRemoteCharacters("name")
        }
        confirmVerified(charactersUC)
        job.cancel()

    }

    @Test
    fun getCharactersError() = runTest {
        coEvery { charactersUC.getRemoteCharacters("name") } returns Result.Error(
            404,
            "error"
        )

        val results = arrayListOf<CharactersState>()

        val job = launch(UnconfinedTestDispatcher()) {
            charactersViewModel.charactersState.toList(results)
        }

        charactersViewModel.getCharacters("name")

        assertNotNull(results)
        assertThat(results[0]).isEqualTo(CharactersState.Loading)
        assertThat(results[1]).isEqualTo(CharactersState.CharactersError(R.string.not_found_error))

        coVerify { charactersUC.getRemoteCharacters("name") }
        job.cancel()
    }

    @Test
    fun getCharactersException() = runTest {
        val exception = Exception("error")
        coEvery { charactersUC.getRemoteCharacters("name") } returns Result.Exception(
            exception
        )

        val results = arrayListOf<CharactersState>()

        val job = launch(UnconfinedTestDispatcher()) {
            charactersViewModel.charactersState.toList(results)
        }

        charactersViewModel.getCharacters("name")

        assertNotNull(results)
        assertThat(results[0]).isEqualTo(CharactersState.Loading)
        assertThat(results[1]).isEqualTo(CharactersState.CharactersError(R.string.connectivity_error))

        coVerify { charactersUC.getRemoteCharacters("name") }
        job.cancel()
    }

    @Test
    fun getCharactersConnectivity() = runTest {
        val exception = IOException()
        coEvery { charactersUC.getRemoteCharacters("name") } returns Result.Exception(
            exception
        )

        val results = arrayListOf<CharactersState>()

        val job = launch(UnconfinedTestDispatcher()) {
            charactersViewModel.charactersState.toList(results)
        }

        charactersViewModel.getCharacters("name")

        assertNotNull(results)
        assertThat(results[0]).isEqualTo(CharactersState.Loading)
        assertThat(results[1]).isEqualTo(CharactersState.CharactersError(R.string.connectivity_error))

        coVerify { charactersUC.getRemoteCharacters("name") }
        job.cancel()
    }

    @Test
    fun getLocalCharacters() = runTest {
        val character = mockk<Character>()
        val characters = listOf(character)
        every { charactersUC.getLocalCharacters() } returns flowOf(characters)

        charactersViewModel.localCharacters.collect {
            assertNotNull(it)
        }
        verify { charactersUC.getLocalCharacters() }
    }
}
