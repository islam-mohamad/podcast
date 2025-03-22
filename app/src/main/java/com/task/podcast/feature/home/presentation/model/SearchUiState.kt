package com.task.podcast.feature.home.presentation.model

data class SearchUiState(
    val isLoading: Boolean = false,
    val query: String = "",
    val searchResults: List<SectionUiModel>? = null
)