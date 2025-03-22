package com.task.podcast.feature.mediaplayer.handler

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MediaPlayerModule {
    @Binds
    abstract fun bindMediaPlayerHandler(impl: MediaPlayerHandlerImpl): MediaPlayerHandler
}