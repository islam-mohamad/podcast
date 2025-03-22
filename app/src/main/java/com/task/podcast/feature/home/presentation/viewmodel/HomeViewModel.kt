package com.task.podcast.feature.home.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.podcast.core.data.di.qualifier.MainDispatcherQualifier
import com.task.podcast.core.data.exception.getMessageResId
import com.task.podcast.core.domain.None
import com.task.podcast.feature.home.domain.usecase.GetHomeSectionsUseCase
import com.task.podcast.feature.home.presentation.model.HomeUiState
import com.task.podcast.feature.home.presentation.model.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeSectionsUseCase: GetHomeSectionsUseCase,
    @MainDispatcherQualifier private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadHomeSections()
    }

    private fun loadHomeSections() {
        viewModelScope.launch(mainDispatcher) {
            _uiState.value = HomeUiState.Loading
            try {
                val data = getHomeSectionsUseCase(None).map { it.toUiModel() }
                    .filter { it.items.isNotEmpty() }
                _uiState.value = HomeUiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.getMessageResId())
            }
        }
    }
}