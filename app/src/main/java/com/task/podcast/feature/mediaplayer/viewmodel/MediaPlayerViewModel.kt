package com.task.podcast.feature.mediaplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.podcast.core.data.di.qualifier.MainDispatcherQualifier
import com.task.podcast.core.presentation.formatTime
import com.task.podcast.feature.mediaplayer.handler.MediaPlayerHandler
import com.task.podcast.feature.mediaplayer.model.MediaPlayerIntent
import com.task.podcast.feature.mediaplayer.model.MediaPlayerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaPlayerViewModel @Inject constructor(
    private val mediaPlayerHandler: MediaPlayerHandler,
    @MainDispatcherQualifier private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(MediaPlayerState())
    val uiState: StateFlow<MediaPlayerState> = _uiState

    private val _isPlayerVisible = MutableStateFlow(false)
    val isPlayerVisible: StateFlow<Boolean> = _isPlayerVisible

    init {
        mediaPlayerHandler.apply {
            onPlaybackStarted = {
                _uiState.update { it.copy(isPlaying = true) }
            }
            onPlaybackCompleted = {
                _uiState.update { state ->
                    state.copy(
                        isPlaying = false,
                        currentProgress = 0f,
                        formattedCurrentTime = 0.formatTime()
                    )
                }
            }
            onPlaybackError = {
                _uiState.update { it.copy(isPlaying = false) }
            }
            onPositionUpdate = { positionMs, durationMs ->
                _uiState.update { state ->
                    state.copy(
                        currentProgress = if (durationMs > 0) positionMs.toFloat() / durationMs.toFloat() else 0f,
                        formattedCurrentTime = positionMs.formatTime(),
                        formattedTotalTime = durationMs.formatTime(),
                        durationMs = durationMs
                    )
                }
            }
            setPositionUpdateInterval(500)
        }
    }

    fun onIntent(intent: MediaPlayerIntent) {
        when (intent) {
            is MediaPlayerIntent.Play -> playMedia(intent.mediaUrl, intent.title, intent.imageUrl)
            is MediaPlayerIntent.Pause -> pauseMedia()
            is MediaPlayerIntent.Seek -> seekTo(intent.position)
        }
    }

    private fun playMedia(url: String, title: String, imageUrl: String) {
        viewModelScope.launch(mainDispatcher) {
            mediaPlayerHandler.startPlayback(url)
            _uiState.update { state ->
                state.copy(
                    isPlaying = true,
                    title = title,
                    imageUrl = imageUrl,
                    currentProgress = 0f,
                    formattedCurrentTime = 0.formatTime(),
                    formattedTotalTime = mediaPlayerHandler.getDuration().formatTime(),
                    durationMs = mediaPlayerHandler.getDuration()
                )
            }
            _isPlayerVisible.value = true
        }
    }

    private fun pauseMedia() {
        viewModelScope.launch(mainDispatcher) {
            mediaPlayerHandler.pausePlayback()
            _uiState.update { it.copy(isPlaying = false) }
        }
    }

    private fun seekTo(position: Int) {
        viewModelScope.launch(mainDispatcher) {
            mediaPlayerHandler.seekTo(position)
            _uiState.update { state ->
                state.copy(
                    formattedCurrentTime = position.formatTime()
                )
            }
        }
    }

    fun hidePlayer() {
        pauseMedia()
        _isPlayerVisible.value = false
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerHandler.releasePlayer()
    }
}