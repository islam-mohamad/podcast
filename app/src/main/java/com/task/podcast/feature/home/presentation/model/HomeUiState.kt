package com.task.podcast.feature.home.presentation.model

import androidx.annotation.StringRes

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val homeSections: List<SectionUiModel>) : HomeUiState
    data class Error(@StringRes val messageResId: Int) : HomeUiState
}