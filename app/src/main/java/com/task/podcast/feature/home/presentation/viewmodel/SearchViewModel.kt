package com.task.podcast.feature.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.podcast.core.data.di.qualifier.MainDispatcherQualifier
import com.task.podcast.core.data.exception.getMessageResId
import com.task.podcast.feature.home.domain.usecase.SearchUseCase
import com.task.podcast.feature.home.presentation.model.SearchIntent
import com.task.podcast.feature.home.presentation.model.SearchUiEvent
import com.task.podcast.feature.home.presentation.model.SearchUiState
import com.task.podcast.feature.home.presentation.model.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    @MainDispatcherQualifier private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<SearchUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val queryFlow = MutableStateFlow("")

    init {
        observeQuery()
    }

    fun processIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.UpdateQuery -> {
                _uiState.update { it.copy(query = intent.query) }
                queryFlow.value = intent.query
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeQuery() {
        viewModelScope.launch(mainDispatcher) {
            queryFlow
                .debounce(200)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isBlank()) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                searchResults = null
                            )
                        }
                        return@collect
                    }
                    _uiState.update { it.copy(isLoading = true) }
                    try {
                        val results = searchUseCase(query)
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                searchResults = results.map { it.toUiModel() }
                                    .filter { it.items.isNotEmpty() })
                        }
                    } catch (e: Exception) {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiEvent.emit(
                            SearchUiEvent.ShowError(e.getMessageResId())
                        )
                    }
                }
        }
    }
}
