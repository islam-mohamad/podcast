package com.task.podcast.feature.home.presentation.model.mapper

import com.task.podcast.feature.home.domain.entity.SectionEntity
import com.task.podcast.feature.home.presentation.model.SectionUiModel
import com.task.podcast.feature.home.presentation.model.enums.SectionType

fun SectionEntity.toUiModel(): SectionUiModel {
    return when (SectionType.from(this.type)) {
        SectionType.BIG_SQUARE -> SectionUiModel.BigSquare(name, content)
        SectionType.TWO_LINES_GRID -> SectionUiModel.TwoLinesGrid(name, content)
        SectionType.QUEUE -> SectionUiModel.Queue(name, content)
        SectionType.SQUARE -> SectionUiModel.Square(name, content)
        SectionType.UNKNOWN -> SectionUiModel.Unknown(name, content)
    }
}