package com.devdaniel.marvelapp.ui

import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.SmallTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.devdaniel.marvelapp.R
import com.devdaniel.marvelapp.base.BaseUITest
import com.devdaniel.marvelapp.data.local.CharactersDao
import com.devdaniel.marvelapp.data.local.entities.CharacterEntity
import com.devdaniel.marvelapp.idleresources.waitUntilViewIsNotDisplayed
import com.devdaniel.marvelapp.network.FILE_SUCCESS_CHARACTERS_RESPONSE
import com.devdaniel.marvelapp.network.mockResponse
import com.devdaniel.marvelapp.ui.characters.CharactersFragment
import com.devdaniel.marvelapp.util.checkViewInRecyclerWithIdAndTextIsDisplayed
import com.devdaniel.marvelapp.util.checkViewWithIdAndTextIsDisplayed
import com.devdaniel.marvelapp.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.QueueDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.net.HttpURLConnection
import javax.inject.Inject

@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
class CharactersFragmentTest : BaseUITest(dispatcher = QueueDispatcher()) {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val successCharactersResponse: MockResponse
        get() = mockResponse(FILE_SUCCESS_CHARACTERS_RESPONSE, HttpURLConnection.HTTP_OK)

    private val failureCharactersResponse: MockResponse
        get() = mockResponse(FILE_SUCCESS_CHARACTERS_RESPONSE, HttpURLConnection.HTTP_UNAVAILABLE)

    @Inject
    lateinit var charactersDao: CharactersDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        hiltRule.inject()
    }

    override fun tearDown() {
        super.tearDown()
        clearMocks(charactersDao)
    }

    private fun launchFragment() {
        launchFragmentInHiltContainer<CharactersFragment>()
    }

    @Test
    @SmallTest
    fun when_characters_is_displayed_should_show_name() {
        launchFragment()
        enqueueResponses(successCharactersResponse)
        waitUntilViewIsNotDisplayed(withId(R.id.lavLoader))
        checkViewInRecyclerWithIdAndTextIsDisplayed(
            viewId = R.id.rcvCharacters,
            viewText = "3-D Man",
            position = 0
        )
        checkViewInRecyclerWithIdAndTextIsDisplayed(
            viewId = R.id.rcvCharacters,
            viewText = "A.I.M.",
            position = 2
        )
    }

    @Test
    @SmallTest
    fun when_characters_is_not_displayed_should_show_local_characters() {
        val characterEntity = mockk<CharacterEntity>()
        val charactersDB = listOf(characterEntity)
        every { characterEntity.id } returns 123
        every { characterEntity.name } returns "A.I.M."
        every { characterEntity.modified } returns "modified"
        every { characterEntity.description } returns "description"
        every { characterEntity.thumbnail } returns "thumbnail"
        every { characterEntity.comicAvailable } returns 1
        every { characterEntity.seriesAvailable } returns 2
        every { characterEntity.storiesAvailable } returns 3
        coEvery { charactersDao.getAllCharacters() } returns charactersDB
        launchFragment()
        enqueueResponses(failureCharactersResponse)
        waitUntilViewIsNotDisplayed(withId(R.id.lavLoader))
        checkViewInRecyclerWithIdAndTextIsDisplayed(
            viewId = R.id.rcvCharacters,
            viewText = "A.I.M.",
            position = 0
        )
    }

    @Test
    @SmallTest
    fun when_characters_failure_should_show_screen_error() {
        coEvery { charactersDao.getAllCharacters() } returns listOf()
        launchFragment()
        enqueueResponses(failureCharactersResponse)
        waitUntilViewIsNotDisplayed(withId(R.id.lavLoader))
        checkViewWithIdAndTextIsDisplayed(
            viewId = R.id.txtErrorTitle,
            viewText = R.string.service_unavailable_error
        )
    }
}
