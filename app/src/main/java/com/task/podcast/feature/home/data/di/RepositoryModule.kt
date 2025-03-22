package com.task.podcast.feature.home.data.di

import com.task.podcast.feature.home.data.repository.SectionsRepositoryImpl
import com.task.podcast.feature.home.domain.repository.SectionsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindSectionsRepository(impl: SectionsRepositoryImpl): SectionsRepository
}