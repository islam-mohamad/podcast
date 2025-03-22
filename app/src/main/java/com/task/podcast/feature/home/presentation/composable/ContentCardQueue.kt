package com.task.podcast.feature.home.presentation.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
fun ContentCardQueue(
    content: ContentEntity, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = radius.radiusS,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation.elevationDefault)
    ) {
        Row(
            modifier = Modifier.padding(spaces.spaceS),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                AsyncImage(
                    model = content.avatarUrl,
                    contentDescription = content.name,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.45f)
                        .clip(radius.radiusS)
                        .background(Color.DarkGray)
                        .height(spaces.space10Xl)
                )
                content.audioUrl?.let {
                    Image(
                        modifier = Modifier
                            .padding(spaces.spaceS)
                            .align(Alignment.BottomStart)
                            .size(spaces.space3Xl),
                        painter = painterResource(R.drawable.ic_play_cricle),
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.width(spaces.spaceS))
            Column {
                Text(
                    text = content.name, style = MaterialTheme.typography.titleMedium, maxLines = 1
                )
                Spacer(modifier = Modifier.height(spaces.spaceXs))
                Text(
                    text = content.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                if (content.duration > 1000) {
                    Spacer(modifier = Modifier.height(spaces.spaceS))
                    Text(
                        text = content.duration.formatTime(), modifier = Modifier
                            .background(color = PodcastTheme.colors.Pink80, shape = radius.radiusS)
                            .padding(
                                horizontal = spaces.spaceL, vertical = spaces.space2Xs
                            )
                    )
                }

            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ContentCardQueuePreview() {
    val dummyContent = ContentEntity(
        podcastId = "1",
        name = "Sample Podcast",
        description = "A short description for preview purposes.",
        avatarUrl = "https://via.placeholder.com/150",
        episodeCount = "10",
        duration = 60,
        language = "en",
        audioUrl = "https://via.placeholder.com/150",
        priority = "1",
        popularityScore = "80",
        score = "4.5"
    )
    ContentCardQueue(content = dummyContent, modifier = Modifier.padding(spaces.spaceL))
}