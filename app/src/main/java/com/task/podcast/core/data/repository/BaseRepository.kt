package com.task.podcast.core.data.repository

import kotlinx.coroutines.flow.Flow

interface BaseRepository {
    fun <T> requestHandler(fetch: suspend () -> T): Flow<T>
    suspend fun <T> runOnIO(fetch: suspend () -> T): T
}