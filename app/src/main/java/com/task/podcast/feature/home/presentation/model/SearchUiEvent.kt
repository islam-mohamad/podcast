package com.task.podcast.feature.home.presentation.model

import androidx.annotation.StringRes

sealed class SearchUiEvent {
    data class ShowError(@StringRes val messageResId: Int) : SearchUiEvent()
}