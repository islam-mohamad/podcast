package com.task.podcast.feature.mediaplayer.model

import androidx.compose.runtime.Immutable

@Immutable
data class MediaPlayerState(
    val isPlaying: Boolean = false,
    val title: String = "",
    val currentProgress: Float = 0f,
    val durationMs: Int = 0,
    val mediaUrl: String = "",
    val imageUrl: String = "",
    val formattedCurrentTime: String = "00:00",
    val formattedTotalTime: String = "00:00"
)