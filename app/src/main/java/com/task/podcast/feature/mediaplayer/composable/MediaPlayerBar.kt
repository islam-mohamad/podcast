package com.task.podcast.feature.mediaplayer.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.task.podcast.R
import com.task.podcast.feature.mediaplayer.model.MediaPlayerState
import com.task.podcast.ui.theme.PodcastTheme.spaces

@Composable
fun MediaPlayerBar(
    state: MediaPlayerState,
    onPlayPauseClick: () -> Unit,
    onSeek: (Int) -> Unit,
    onClose: () -> Unit
) {
    var sliderProgress by remember { mutableFloatStateOf(state.currentProgress) }

    LaunchedEffect(state.currentProgress) {
        sliderProgress = state.currentProgress
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(spaces.spaceS)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = state.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(spaces.space6Xl)
                    .clip(RoundedCornerShape(spaces.spaceS))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = spaces.spaceS)
            ) {
                Text(
                    text = state.title,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${state.formattedCurrentTime} / ${state.formattedTotalTime}",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            IconButton(onClick = onPlayPauseClick) {
                Icon(
                    painter = painterResource(
                        if (state.isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                    ),
                    contentDescription = "Play/Pause",
                    tint = Color.White
                )
            }

            IconButton(onClick = onClose) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(spaces.spaceS))


        Slider(
            value = sliderProgress,
            onValueChange = { newValue ->
                sliderProgress = newValue
            },
            onValueChangeFinished = {
                val seekPosition = (sliderProgress * state.durationMs).toInt()
                onSeek(seekPosition)
            },
            colors = SliderDefaults.colors(
                thumbColor = Color.Black,
                activeTrackColor = Color.DarkGray,
                inactiveTrackColor = Color.Gray,
            )
        )
    }
}


@Preview
@Composable
private fun PreviewMediaPlayer() {
    MediaPlayerBar(
        MediaPlayerState(
            true,
            "title", 0.2f
        ), {}, {}, {})
}