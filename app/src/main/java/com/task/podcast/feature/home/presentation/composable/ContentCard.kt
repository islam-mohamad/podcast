package com.task.podcast.feature.home.presentation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.task.podcast.feature.home.domain.entity.ContentEntity
import com.task.podcast.ui.theme.PodcastTheme.spaces

@Composable
fun ContentCard(
    content: ContentEntity,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(spaces.spaceS),
        elevation = CardDefaults.cardElevation(defaultElevation = spaces.spaceXs)
    ) {
        Column(modifier = Modifier.padding(spaces.spaceS)) {
            AsyncImage(
                model = content.avatarUrl,
                contentDescription = content.name,
                modifier = Modifier
                    .fillMaxSize()
            )
            Spacer(modifier = Modifier.height(spaces.spaceS))
            Text(
                text = content.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(spaces.spaceXs))
            Text(
                text = content.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewContentCard() {
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
        score = "4.5"
    )
    ContentCard(content = dummyContent, modifier = Modifier.padding(spaces.spaceL))
}