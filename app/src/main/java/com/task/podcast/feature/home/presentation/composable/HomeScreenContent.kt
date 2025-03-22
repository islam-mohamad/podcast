package com.task.podcast.feature.home.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.task.podcast.R
import com.task.podcast.feature.home.domain.entity.ContentEntity
import com.task.podcast.feature.home.presentation.model.HomeUiState
import com.task.podcast.feature.home.presentation.model.SectionUiModel
import com.task.podcast.ui.theme.PodcastTheme.spaces


@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onSectionClick: (ContentEntity) -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onSearchClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (uiState) {
                    is HomeUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    is HomeUiState.Error -> {
                        Text(
                            text = stringResource(uiState.messageResId),
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    is HomeUiState.Success -> {
                        val sections = uiState.homeSections
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(spaces.spaceL),
                            verticalArrangement = Arrangement.spacedBy(spaces.spaceL)
                        ) {
                            items(
                                items = sections,
                                key = { it.sectionName }
                            ) { section ->
                                SectionComposable(
                                    section = section,
                                    onSectionClick = onSectionClick
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewHomeScreenContent_Success() {
    val dummyContent1 = ContentEntity(
        podcastId = "1",
        name = "Sample Podcast",
        description = "A short description for preview purposes.",
        avatarUrl = "https://via.placeholder.com/150",
        episodeCount = "10",
        duration = 60,
        language = "en",
        priority = "1",
        popularityScore = "80",
        score = "4.5"
    )

    val dummyContent2 = ContentEntity(
        podcastId = "2",
        name = "Sample Podcast",
        description = "A short description for preview purposes.",
        avatarUrl = "https://via.placeholder.com/150",
        episodeCount = "10",
        duration = 60,
        language = "en",
        priority = "1",
        popularityScore = "80",
        score = "4.5"
    )
    val dummySection = SectionUiModel.BigSquare(
        sectionName = "Sample Section",
        items = listOf(dummyContent1, dummyContent2)
    )
    HomeScreenContent(
        uiState = HomeUiState.Success(
            listOf(dummySection)
        )
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewHomeScreenContent_Loading() {
    HomeScreenContent(uiState = HomeUiState.Loading)
}

@Composable
@Preview(showBackground = true)
fun PreviewHomeScreenContent_Error() {
    HomeScreenContent(uiState = HomeUiState.Error(R.string.default_error_msg))
}