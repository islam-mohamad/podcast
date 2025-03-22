package com.task.podcast.feature.home.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.task.podcast.feature.home.domain.entity.ContentEntity
import com.task.podcast.feature.home.presentation.model.SearchIntent
import com.task.podcast.feature.home.presentation.model.SearchUiState
import com.task.podcast.feature.home.presentation.model.SectionUiModel
import com.task.podcast.ui.theme.PodcastTheme.spaces

@Composable
fun SearchScreenContent(
    uiState: SearchUiState,
    onIntent: (SearchIntent) -> Unit,
    onItemClicked: (ContentEntity) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = uiState.query,
            onValueChange = { newQuery ->
                onIntent(SearchIntent.UpdateQuery(newQuery))
            },
            label = { Text("بحث") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(spaces.spaceL)
        )
        Spacer(modifier = Modifier.height(spaces.spaceS))
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.searchResults != null -> {
                val sections = uiState.searchResults
                if (sections.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("لا توجد نتائج", style = MaterialTheme.typography.bodyLarge)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(spaces.spaceL),
                        verticalArrangement = Arrangement.spacedBy(spaces.spaceL)
                    ) {
                        items(
                            items = sections,
                            key = { it.sectionName }
                        ) { section ->
                            SectionComposable(section = section, onSectionClick = onItemClicked)
                        }
                    }
                }
            }

            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("ابدأ بالبحث", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewSearchScreenContent_Success() {
    val dummyContent1 = ContentEntity(
        podcastId = "1",
        name = "Preview Podcast",
        description = "Preview description",
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
        name = "Preview Podcast",
        description = "Preview description",
        avatarUrl = "https://via.placeholder.com/150",
        episodeCount = "10",
        duration = 60,
        language = "en",
        priority = "1",
        popularityScore = "80",
        score = "4.5"
    )
    val dummySection = SectionUiModel.Queue(
        sectionName = "Sample Section",
        items = listOf(dummyContent1, dummyContent2)
    )
    val dummyState = SearchUiState(
        isLoading = false,
        query = "Preview",
        searchResults = listOf(dummySection)
    )
    SearchScreenContent(
        uiState = dummyState,
        onIntent = {},
        onItemClicked = {}
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewSearchScreenContent_Loading() {
    val dummyState = SearchUiState(
        isLoading = true,
        query = "Loading",
        searchResults = null
    )
    SearchScreenContent(
        uiState = dummyState,
        onIntent = {},
        onItemClicked = {}
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewSearchScreenContent_Empty() {
    val dummyState = SearchUiState(
        isLoading = false,
        query = "",
        searchResults = null
    )
    SearchScreenContent(
        uiState = dummyState,
        onIntent = {},
        onItemClicked = {}
    )
}