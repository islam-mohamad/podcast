package com.task.podcast.feature.home.presentation.composable

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.task.podcast.feature.home.domain.entity.ContentEntity
import com.task.podcast.feature.home.presentation.model.SectionUiModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SectionComposableTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testContent = listOf(
        ContentEntity(
            podcastId = "121493675",
            episodeId = "e35071e5-c224-58b8-9a33-93ab500c4832",
            audiobookId = null,
            articleId = null,
            name = "Test Podcast 1",
            description = "NPR News: 07-23-2024 4AM EDT...",
            avatarUrl = "https://media.npr.org/assets/img/2023/03/01/npr-news-now_square.png",
            duration = 300,
            language = "en",
            score = "214.18567",
            episodeCount = "",
            priority = "5",
            popularityScore = "9",
            audioUrl = "https://chrt.fm/track/138C95/prfx.byspotify.com/e/play.podtrac.com/npr-500005/traffic.megaphone.fm/NPR6248253268.mp3",
            releaseDate = "2024-07-23T08:00:00.000Z",
            authorName = null
        ),
        ContentEntity(
            podcastId = "121493676",
            episodeId = "e35071e5-c224-58b8-9a33-93ab500c4833",
            audiobookId = null,
            articleId = null,
            name = "Test Podcast 2",
            description = "NPR News: 08-23-2024 4AM EDT...",
            avatarUrl = "https://media.npr.org/assets/img/2023/03/01/npr-news-now_square.png",
            duration = 300,
            language = "en",
            score = "214.18569",
            episodeCount = "",
            priority = "2",
            popularityScore = "8",
            audioUrl = "https://chrt.fm/track/138C95/prfx.byspotify.com/e/play.podtrac.com/npr-500005/traffic.megaphone.fm/NPR6248253268.mp3",
            releaseDate = "2024-08-23T08:00:00.000Z",
            authorName = null
        )
    )

    @Test
    fun displayQueueSection_correctly() {
        var clickedContent: ContentEntity? = null
        val section = SectionUiModel.Queue("Queue Section", testContent)

        composeTestRule.setContent {
            SectionComposable(
                section = section,
                onSectionClick = { clickedContent = it }
            )
        }

        // Verify section title
        composeTestRule.onNodeWithText("Queue Section").assertExists()

        // Verify content items
        testContent.forEach {
            composeTestRule.onNodeWithText(it.name).assertExists()
            composeTestRule.onNodeWithText(it.description).assertExists()
        }

        // Test click interaction
        composeTestRule.onAllNodesWithText("Test Podcast 1")[0]
            .performClick()
        assert(clickedContent == testContent[0])
    }

    @Test
    fun displayTwoLinesGridSection_correctly() {
        val section = SectionUiModel.TwoLinesGrid("Grid Section", testContent)

        composeTestRule.setContent {
            SectionComposable(section = section)
        }

        composeTestRule.onNodeWithText("Grid Section").assertExists()
        composeTestRule.onAllNodesWithText("Test Podcast 1").assertCountEquals(1)
        composeTestRule.onAllNodesWithText("Test Podcast 2").assertCountEquals(1)
    }

    @Test
    fun displayBigSquareSection_correctly() {
        var clickedContent: ContentEntity? = null
        val section = SectionUiModel.BigSquare("Big Square", testContent)

        composeTestRule.setContent {
            SectionComposable(
                section = section,
                onSectionClick = { clickedContent = it }
            )
        }

        composeTestRule.onNodeWithText("Big Square").assertExists()
            composeTestRule.onAllNodesWithText("Test Podcast 1").assertCountEquals(1)

        // Test click interaction
        composeTestRule.onAllNodesWithText("Test Podcast 1")[0]
            .performClick()
        assert(clickedContent == testContent[0])
    }

    @Test
    fun displayUnknownSection_correctly() {
        var clickedContent: ContentEntity? = null
        val section = SectionUiModel.Unknown("Unknown Section", testContent)

        composeTestRule.setContent {
            SectionComposable(
                section = section,
                onSectionClick = { clickedContent = it }
            )
        }

        composeTestRule.onNodeWithText("Unknown Section").assertExists()
        composeTestRule.onAllNodesWithText("Test Podcast 1").assertCountEquals(1)

        // Test click interaction
        composeTestRule.onAllNodesWithText("Test Podcast 1")[0]
            .performClick()
        assert(clickedContent == testContent[0])
    }
}