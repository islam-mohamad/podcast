package com.task.podcast.core.data.exception

sealed class PodcastException : Exception() {
    data object ConnectionError : PodcastException()
    data object UnAuthorizedError : PodcastException()
    data object InternalServerError : PodcastException()
}