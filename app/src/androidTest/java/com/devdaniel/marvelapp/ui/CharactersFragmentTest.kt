package com.devdaniel.marvelapp.ui

import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.SmallTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.devdaniel.marvelapp.R
import com.devdaniel.marvelapp.base.BaseUITest
import com.devdaniel.marvelapp.idleresources.waitUntilViewIsNotDisplayed
import com.devdaniel.marvelapp.network.FILE_SUCCESS_CHARACTERS_RESPONSE
import com.devdaniel.marvelapp.network.mockResponse
import com.devdaniel.marvelapp.ui.characters.CharactersFragment
import com.devdaniel.marvelapp.util.checkViewInRecyclerWithIdAndTextIsDisplayed
import com.devdaniel.marvelapp.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.QueueDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.net.HttpURLConnection

@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
class CharactersFragmentTest : BaseUITest(dispatcher = QueueDispatcher()) {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val successCharactersResponse: MockResponse
        get() = mockResponse(FILE_SUCCESS_CHARACTERS_RESPONSE, HttpURLConnection.HTTP_OK)

    private val failureCharactersResponse: MockResponse
        get() = mockResponse(FILE_SUCCESS_CHARACTERS_RESPONSE, HttpURLConnection.HTTP_UNAVAILABLE)

    @Before
    fun setUp() {
        launchFragment()
        hiltRule.inject()
    }

    private fun launchFragment() {
        launchFragmentInHiltContainer<CharactersFragment>()
    }

    @Test
    @SmallTest
    fun when_characters_is_displayed_should_show_name() {
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
}
