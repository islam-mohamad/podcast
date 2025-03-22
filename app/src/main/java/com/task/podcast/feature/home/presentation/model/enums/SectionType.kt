package com.task.podcast.feature.home.presentation.model.enums

enum class SectionType {
    BIG_SQUARE,
    TWO_LINES_GRID,
    QUEUE,
    SQUARE,
    UNKNOWN;

    companion object {
        fun from(type: String?): SectionType {
            return when (type?.lowercase()?.replace(" ", "_")) {
                "big_square" -> BIG_SQUARE
                "2_lines_grid" -> TWO_LINES_GRID
                "queue" -> QUEUE
                "square" -> SQUARE
                else -> UNKNOWN
            }
        }
    }
}