package com.task.podcast.feature.home.presentation.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.task.podcast.R
import com.task.podcast.core.presentation.formatTime
import com.task.podcast.feature.home.domain.entity.ContentEntity
import com.task.podcast.ui.theme.PodcastTheme
import com.task.podcast.ui.theme.PodcastTheme.elevation
import com.task.podcast.ui.theme.PodcastTheme.radius
import com.task.podcast.ui.theme.PodcastTheme.spaces

@Composable
fun ContentCardBigSquare(
    content: ContentEntity, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = radius.radiusS,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation.elevationDefault)
    ) {
        Box {
            AsyncImage(
                model = content.avatarUrl,
                contentDescription = content.name,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(radius.radiusS)
                    .background(Color.DarkGray)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spaces.spaceM)
                    .align(Alignment.BottomStart)
            ) {
                Text(
                    text = content.name, style = MaterialTheme.typography.titleMedium, maxLines = 1
                )
                if (content.duration > 1000) {
                    Spacer(modifier = Modifier.height(spaces.spaceS))
                    Text(
                        text = content.duration.formatTime(), modifier = Modifier
                            .background(color = PodcastTheme.colors.Pink80, shape = radius.radiusL)
                            .padding(
                                horizontal = spaces.spaceL, vertical = spaces.space2Xs
                            )
                            .align(Alignment.End)
                    )
                }
            }

            content.audioUrl?.let {
                Image(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(spaces.space4Xl),
                    painter = painterResource(R.drawable.ic_play_cricle),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ContentCardBigSquarePreview() {
    val dummyContent = ContentEntity(
        podcastId = "1",
        name = "Sample Podcast",
        description = "A short description for preview purposes.",
        avatarUrl = "https://via.placeholder.com/150",
        episodeCount = "10",
        duration = 60,
        language = "en",
        priority = "1",
        audioUrl = "https://via.placeholder.com/150",
        popularityScore = "80",
        score = "4.5",
        authorName = "Ahmed Mohamed"
    )
    ContentCardBigSquare(content = dummyContent, modifier = Modifier.padding(spaces.spaceL))
}