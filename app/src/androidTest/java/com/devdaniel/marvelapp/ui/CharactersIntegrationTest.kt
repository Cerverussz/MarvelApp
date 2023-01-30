package com.devdaniel.marvelapp.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.SmallTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.devdaniel.marvelapp.R
import com.devdaniel.marvelapp.base.BaseUITest
import com.devdaniel.marvelapp.data.local.CharactersDao
import com.devdaniel.marvelapp.data.local.entities.CharacterEntity
import com.devdaniel.marvelapp.idleresources.waitUntilViewIsDisplayed
import com.devdaniel.marvelapp.idleresources.waitUntilViewIsNotDisplayed
import com.devdaniel.marvelapp.network.FILE_SUCCESS_CHARACTER_DETAIL_2_RESPONSE
import com.devdaniel.marvelapp.network.mockResponse
import com.devdaniel.marvelapp.util.performClickInViewPositionRecyclerView
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.net.HttpURLConnection
import javax.inject.Inject
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
class CharactersIntegrationTest : BaseUITest(dispatcher = comicsDispatcher) {

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    var activityScenarioRule: ActivityScenarioRule<HomeActivity> =
        ActivityScenarioRule(HomeActivity::class.java)

    @get:Rule(order = 3)
    val composeTestRule = createAndroidComposeRule<HomeActivity>()

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

    @Test
    @SmallTest
    fun when_screen_is_created_and_click_on_character_should_show_details() {
        val characterEntity = mockk<CharacterEntity>()
        val charactersDB = listOf(characterEntity)
        every { characterEntity.id } returns 123
        every { characterEntity.name } returns "Iron Man"
        every { characterEntity.modified } returns "modified"
        every { characterEntity.description } returns "description"
        every { characterEntity.thumbnail } returns "thumbnail"
        every { characterEntity.comicAvailable } returns 1
        every { characterEntity.seriesAvailable } returns 2
        every { characterEntity.storiesAvailable } returns 3
        coEvery { charactersDao.getAllCharacters() } returns charactersDB
        waitUntilViewIsDisplayed(withId(R.id.rcvCharacters))
        performClickInViewPositionRecyclerView(
            viewId = R.id.rcvCharacters,
            childViewId = R.id.cnlContainerItem,
            position = 0
        )
        waitUntilViewIsNotDisplayed(withId(R.id.lavLoader))
        composeTestRule.onNodeWithText("Wolverine Saga (2009) #7").assertIsDisplayed()
    }
}

private val comicsDispatcher by lazy {
    object : Dispatcher() {
        @Throws(InterruptedException::class)
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when  {
                request.path?.contains(SUCCESS_COMICS) ?: false -> {
                    mockResponse(
                        FILE_SUCCESS_CHARACTER_DETAIL_2_RESPONSE,
                        HttpURLConnection.HTTP_OK
                    )
                }
                else -> {
                    mockResponse(
                        String(),
                        HttpURLConnection.HTTP_BAD_REQUEST
                    )
                }
            }
        }
    }
}

private const val SUCCESS_COMICS =
    "/v1/public/characters/123/comics?format=comic&limit=10&apikey=e5353166155e4561e37e207ae1bff612"
