package com.task.podcast.feature.home.domain.repository

import com.task.podcast.feature.home.domain.entity.SectionEntity

interface SectionsRepository {
    suspend fun getHomeSections(): List<SectionEntity>
    suspend fun search(query: String): List<SectionEntity>
}