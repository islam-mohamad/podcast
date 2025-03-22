package com.task.podcast.feature.home.domain.entity

data class SectionEntity(
    val name: String,
    val type: String,
    val contentType: String,
    val order: String,
    val content: List<ContentEntity>
)