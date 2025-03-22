package com.task.podcast.feature.mediaplayer.model

sealed class MediaPlayerIntent {
    data class Play(val mediaUrl: String, val title: String, val imageUrl: String) : MediaPlayerIntent()
    object Pause : MediaPlayerIntent()
    data class Seek(val position: Int) : MediaPlayerIntent()
}