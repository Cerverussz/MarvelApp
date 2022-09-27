package com.devdaniel.marvelapp.ui

import androidx.activity.viewModels
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.devdaniel.marvelapp.R
import com.devdaniel.marvelapp.base.BaseUITest
import com.devdaniel.marvelapp.network.FILE_SUCCESS_CHARACTER_DETAIL_2_RESPONSE
import com.devdaniel.marvelapp.network.mockResponse
import com.devdaniel.marvelapp.ui.detail.CharacterDetail
import com.devdaniel.marvelapp.ui.detail.CharacterDetailViewModel
import com.devdaniel.marvelapp.ui.model.InfoCharacter
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.QueueDispatcher
import org.junit.Rule
import org.junit.Test
import java.net.HttpURLConnection

@HiltAndroidTest
class CharacterDetailFragmentTest : BaseUITest(dispatcher = QueueDispatcher()) {

    @get:Rule
    val hiltAndroidTest = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<HomeActivity>()

    private val successCharactersResponse: MockResponse
        get() = mockResponse(FILE_SUCCESS_CHARACTER_DETAIL_2_RESPONSE, HttpURLConnection.HTTP_OK)

    private fun navigateToCharacterDetail() {
        hiltAndroidTest.inject()
        val character = mockk<InfoCharacter>()
        every { character.id } returns 123
        composeTestRule.setContent {
            CharacterDetail(
                characterDetailViewModel = composeTestRule.activity.viewModels<CharacterDetailViewModel>().value,
                infoCharacter = character
            ) {}
        }

        composeTestRule.activityRule.scenario.onActivity {
            it.findNavController(R.id.nav_graph)
                .navigate(
                    R.id.characterDetailFragment,
                    bundleOf("infoCharacter" to character)
                )
        }
    }

    @Test
    fun when_character_is_displayed_should_show_image_character() {
        enqueueResponses(successCharactersResponse)
        navigateToCharacterDetail()
        listOf("imageCharacter").onEach {
            composeTestRule.onNodeWithTag(it).assertIsDisplayed()
        }
    }
}
