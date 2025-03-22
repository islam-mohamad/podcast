package com.task.podcast.feature.home.presentation.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.task.podcast.feature.mediaplayer.composable.MediaPlayerBar
import com.task.podcast.feature.mediaplayer.model.MediaPlayerIntent
import com.task.podcast.feature.mediaplayer.viewmodel.MediaPlayerViewModel

@Composable
fun MainNavHost(navController: NavHostController = rememberNavController()) {
    val mediaPlayerViewModel: MediaPlayerViewModel = hiltViewModel()
    val uiState = mediaPlayerViewModel.uiState.collectAsState().value
    val isPlayerVisible = mediaPlayerViewModel.isPlayerVisible.collectAsState().value

    Box(Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeScreen(
                    onSectionClick = { item ->
                        item.audioUrl?.let {
                            mediaPlayerViewModel.onIntent(
                                MediaPlayerIntent.Play(
                                    mediaUrl = it,
                                    title = item.name,
                                    imageUrl = item.avatarUrl
                                )
                            )
                        }
                    },
                    onSearchClick = {
                        navController.navigate("search")
                        mediaPlayerViewModel.onIntent(MediaPlayerIntent.Pause)
                    }
                )
            }
            composable("search") {
                SearchScreen(
                    onItemClicked = { item ->
                        item.audioUrl?.let {
                            mediaPlayerViewModel.onIntent(
                                MediaPlayerIntent.Play(
                                    mediaUrl = it,
                                    title = item.name,
                                    imageUrl = item.avatarUrl
                                )
                            )
                        }
                    }
                )
            }
        }

        if (isPlayerVisible) {
            MediaPlayerBar(
                state = uiState,
                onPlayPauseClick = {
                    if (uiState.isPlaying) mediaPlayerViewModel.onIntent(MediaPlayerIntent.Pause)
                    else mediaPlayerViewModel.onIntent(
                        MediaPlayerIntent.Play(
                            title = uiState.title,
                            imageUrl = uiState.imageUrl,
                            mediaUrl = uiState.mediaUrl
                        )
                    )
                },
                onSeek = { position -> mediaPlayerViewModel.onIntent(MediaPlayerIntent.Seek(position)) },
                onClose = { mediaPlayerViewModel.hidePlayer() }
            )
        }
    }
}