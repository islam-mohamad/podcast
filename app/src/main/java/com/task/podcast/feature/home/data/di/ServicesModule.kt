package com.task.podcast.feature.home.data.di

import com.task.podcast.feature.home.data.source.remote.HomeApiService
import com.task.podcast.feature.home.data.source.remote.SearchApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object ServicesModule {
    @Provides
    fun provideHomeApiService(retrofit: Retrofit):HomeApiService = retrofit.create(HomeApiService::class.java)

    @Provides
    fun provideSearchApiService(retrofit: Retrofit):SearchApiService = retrofit.create(SearchApiService::class.java)
}