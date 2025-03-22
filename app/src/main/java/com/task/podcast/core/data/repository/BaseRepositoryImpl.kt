package com.task.podcast.core.data.repository

import android.util.Log
import com.task.podcast.core.data.di.qualifier.IoDispatcherQualifier
import com.task.podcast.core.data.exception.mapNetworkErrors
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BaseRepositoryImpl @Inject constructor(@IoDispatcherQualifier private val ioDispatcher: CoroutineDispatcher) :
    BaseRepository {
    override fun <T> requestHandler(fetch: suspend () -> T): Flow<T> = flow {
        try {
            emit(fetch.invoke())
        } catch (throwable: Throwable) {
            Log.e(
                "BaseRepositoryImpl",
                "error happen in requestHandler $throwable"
            )
            throw throwable.mapNetworkErrors()
        }
    }.flowOn(ioDispatcher)

    override suspend fun <T> runOnIO(fetch: suspend () -> T): T {
        return withContext(ioDispatcher) {
            try {
                fetch.invoke()
            } catch (throwable: Throwable) {
                Log.e(
                    "BaseRepositoryImpl",
                    "error happen in runOnIO $throwable"
                )
                throw throwable.mapNetworkErrors()
            }
        }
    }
}