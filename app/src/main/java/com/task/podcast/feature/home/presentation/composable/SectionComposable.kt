package com.task.podcast.feature.home.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.task.podcast.feature.home.domain.entity.ContentEntity
import com.task.podcast.feature.home.presentation.model.SectionUiModel
import com.task.podcast.ui.theme.PodcastTheme.spaces

@Composable
fun SectionComposable(
    section: SectionUiModel,
    onSectionClick: (ContentEntity) -> Unit = {}
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val horizontalPadding = spaces.spaceL

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = section.sectionName,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = horizontalPadding)
        )
        Spacer(modifier = Modifier.height(spaces.spaceS))

        when (section) {

            is SectionUiModel.Queue -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = horizontalPadding),
                    horizontalArrangement = Arrangement.spacedBy(spaces.spaceM)
                ) {
                    itemsIndexed(
                        section.items,
                        key = { index, _ -> "${section.sectionName}_$index" }) { _, content ->
                        ContentCardQueue(
                            content = content,
                            modifier = Modifier
                                .clickable { onSectionClick(content) }
                                .fillParentMaxWidth()
                        )
                    }
                }
            }

            is SectionUiModel.TwoLinesGrid -> {
                val itemWidth = screenWidth * 0.75
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp),
                    horizontalArrangement = Arrangement.spacedBy(spaces.spaceS),
                    verticalArrangement = Arrangement.spacedBy(spaces.spaceM),
                    contentPadding = PaddingValues(horizontal = horizontalPadding)
                ) {
                    itemsIndexed(
                        section.items,
                        key = { index, _ -> "${section.sectionName}_$index" }) { _, content ->
                        ContentCardQueue(
                            content = content,
                            modifier = Modifier
                                .size(itemWidth.dp)
                                .clickable { onSectionClick(content) }
                        )
                    }
                }
            }

            is SectionUiModel.Square -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = horizontalPadding),
                    horizontalArrangement = Arrangement.spacedBy(spaces.spaceS)
                ) {
                    itemsIndexed(
                        section.items,
                        key = { index, _ -> "${section.sectionName}_$index" }) { _, content ->
                        ContentCardSquare(
                            content = content,
                            modifier = Modifier
                                .fillParentMaxWidth(0.5f)
                                .clickable { onSectionClick(content) }
                        )
                    }
                }
            }

            is SectionUiModel.BigSquare -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = horizontalPadding),
                    horizontalArrangement = Arrangement.spacedBy(spaces.spaceS)
                ) {
                    itemsIndexed(
                        section.items,
                        key = { index, _ -> "${section.sectionName}_$index" }) { _, content ->
                        ContentCardBigSquare(
                            content = content,
                            modifier = Modifier
                                .size((screenWidth * 0.6).dp)
                                .clickable { onSectionClick(content) }
                        )
                    }
                }
            }

            is SectionUiModel.Unknown -> {
                val itemSize = 130.dp
                LazyRow(
                    contentPadding = PaddingValues(horizontal = horizontalPadding),
                    horizontalArrangement = Arrangement.spacedBy(spaces.spaceS)
                ) {
                    itemsIndexed(
                        section.items,
                        key = { index, _ -> "${section.sectionName}_$index" }) { _, content ->
                        ContentCard(
                            content = content,
                            modifier = Modifier
                                .size(itemSize)
                                .clickable { onSectionClick(content) }
                        )
                    }
                }
            }
        }
    }
}