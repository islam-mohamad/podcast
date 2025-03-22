package com.task.podcast.feature.home.presentation.model

sealed class SearchIntent {
    data class UpdateQuery(val query: String) : SearchIntent()
}