package com.task.podcast.feature.home.data.repository

import com.task.podcast.core.data.repository.BaseRepository
import com.task.podcast.feature.home.data.model.mapper.toEntity
import com.task.podcast.feature.home.data.source.remote.HomeApiService
import com.task.podcast.feature.home.data.source.remote.SearchApiService
import com.task.podcast.feature.home.domain.entity.SectionEntity
import com.task.podcast.feature.home.domain.repository.SectionsRepository
import javax.inject.Inject

class SectionsRepositoryImpl @Inject constructor(
    private val homeApiService: HomeApiService,
    private val searchApiService: SearchApiService,
    private val baseRepository: BaseRepository
) : SectionsRepository, BaseRepository by baseRepository {
    override suspend fun getHomeSections(): List<SectionEntity> {
        return runOnIO {
            val response = homeApiService.getHomeSections()
            response.sections?.map { it.toEntity() } ?: emptyList()
        }
    }

    override suspend fun search(query: String): List<SectionEntity> {
        return runOnIO {
            val response = searchApiService.search(query)
            response.sections?.map { it.toEntity() } ?: emptyList()
        }
    }
}