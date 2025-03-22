package com.task.podcast.core.data.exception

import com.task.podcast.R
import com.task.podcast.core.data.exception.PodcastException.InternalServerError
import com.task.podcast.core.data.exception.PodcastException.UnAuthorizedError
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.mapNetworkErrors(): PodcastException {
    return when (this) {
        is ConnectException, is SocketTimeoutException, is UnknownHostException, is SocketException,
        is IOException -> PodcastException.ConnectionError

        is HttpException -> {
            when (code()) {
                401 -> UnAuthorizedError
                else -> InternalServerError
            }
        }

        else -> InternalServerError
    }
}

fun Exception.getMessageResId(): Int {
    return when (this) {
        is PodcastException.ConnectionError -> R.string.connection_error_msg
        is UnAuthorizedError -> R.string.unauthorized_error_msg
        else -> R.string.default_error_msg
    }
}