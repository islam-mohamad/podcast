package com.task.podcast.feature.home.presentation.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.task.podcast.feature.home.domain.entity.ContentEntity
import com.task.podcast.feature.home.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    onSectionClick: (ContentEntity) -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState().value

    HomeScreenContent(
        uiState = uiState,
        onSectionClick = onSectionClick,
        onSearchClick = onSearchClick
    )
}