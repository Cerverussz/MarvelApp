package com.devdaniel.marvelapp.ui.characters

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devdaniel.marvelapp.domain.common.Result
import com.devdaniel.marvelapp.domain.model.Character
import com.devdaniel.marvelapp.domain.usecase.CharactersUC
import com.devdaniel.marvelapp.rule.CoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @Test
    fun getCharactersState() {
        assertThat(
            charactersViewModel.charactersState.value
        ).isInstanceOf(CharactersState::class.java)
        assertThat(charactersViewModel.charactersState.value).isEqualTo(CharactersState())
    }

    @Test
    fun getCharactersSuccess() = runTest {
        val character = mockk<Character>()
        val characters = listOf(character)
        coEvery { charactersUC.getRemoteCharacters() } returns Result.Success(characters)

        val results = arrayListOf<CharactersState>()

        val job = launch(UnconfinedTestDispatcher()) {
            charactersViewModel.charactersState.toList(results)
        }

        charactersViewModel.getCharacters()

        assertNotNull(results)
        assertThat(results[0].isLoading).isFalse()
        assertThat(results[1].isLoading).isTrue()

        coVerify { charactersUC.getRemoteCharacters() }
        job.cancel()
    }
}
