package com.task.podcast.feature.home.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.task.podcast.feature.home.domain.entity.ContentEntity
import com.task.podcast.ui.theme.PodcastTheme.elevation
import com.task.podcast.ui.theme.PodcastTheme.radius
import com.task.podcast.ui.theme.PodcastTheme.spaces

@Composable
fun ContentCardSquare(
    content: ContentEntity, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = radius.radiusS,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation.elevationDefault)
    ) {
        AsyncImage(
            model = content.avatarUrl,
            contentDescription = content.name,
            modifier = Modifier
                .fillMaxSize()
                .clip(radius.radiusS)
                .background(Color.DarkGray)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ContentCardSquarePreview() {
    val dummyContent = ContentEntity(
        podcastId = "1",
        name = "Sample Podcast",
        description = "A short description for preview purposes.",
        avatarUrl = "https://via.placeholder.com/150",
        episodeCount = "10",
        duration = 60,
        language = "en",
        priority = "1",
        popularityScore = "80",
        score = "4.5",
        authorName = "Ahmed Mohamed"
    )
    ContentCardSquare(content = dummyContent, modifier = Modifier.padding(spaces.spaceL))
}