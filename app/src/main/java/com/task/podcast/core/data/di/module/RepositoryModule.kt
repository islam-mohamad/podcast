package com.task.podcast.core.data.di.module

import com.task.podcast.core.data.repository.BaseRepository
import com.task.podcast.core.data.repository.BaseRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindBaseRepository(impl: BaseRepositoryImpl): BaseRepository
}