package com.task.podcast.feature.mediaplayer.handler

interface MediaPlayerHandler {
    var onPlaybackStarted: () -> Unit
    var onPlaybackCompleted: () -> Unit
    var onPlaybackError: (String) -> Unit
    var onPositionUpdate: (Int, Int) -> Unit

    fun startPlayback(url: String)
    fun pausePlayback()
    fun seekTo(position: Int)
    fun getDuration(): Int
    fun releasePlayer()
    fun setPositionUpdateInterval(intervalMs: Long)
}
