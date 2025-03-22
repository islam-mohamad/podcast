package com.task.podcast.feature.home.presentation.model

import com.task.podcast.feature.home.domain.entity.ContentEntity

sealed class SectionUiModel {
    abstract val sectionName: String
    abstract val items: List<ContentEntity>

    data class BigSquare(
        override val sectionName: String,
        override val items: List<ContentEntity>
    ) : SectionUiModel()

    data class TwoLinesGrid(
        override val sectionName: String,
        override val items: List<ContentEntity>
    ) : SectionUiModel()

    data class Queue(
        override val sectionName: String,
        override val items: List<ContentEntity>
    ) : SectionUiModel()

    data class Square(
        override val sectionName: String,
        override val items: List<ContentEntity>
    ) : SectionUiModel()

    data class Unknown(
        override val sectionName: String,
        override val items: List<ContentEntity>
    ) : SectionUiModel()
}